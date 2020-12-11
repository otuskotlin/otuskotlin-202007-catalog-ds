package ru.otus.otuskotlin.catalogue.backend.logics.categories

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.errors.GeneralError
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.CorHandler
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import ru.otus.otuskotlin.catalogue.backend.logics.categories.stubs.categoryCreateStub
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.prepareResponse
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.setRepoByWorkMode

class CategoryCreateChain(
    private val categoryRepoTest: ICategoryRepository,
    private val categoryRepoProd: ICategoryRepository
) {

    suspend fun exec(ctx: CategoryContext) = chain.exec(ctx.apply {
        categoryRepoTest = this@CategoryCreateChain.categoryRepoTest
        categoryRepoProd = this@CategoryCreateChain.categoryRepoProd
    })

    companion object{
        private val chain = corProc<CategoryContext>{
            // pipeline init
            exec { status = ContextStatus.RUNNING }

            // set repo in context
            processor {
                exec(setRepoByWorkMode())
            }

            // stub handling
            exec(categoryCreateStub)

            // job with db
            handler {
                isMatchable { status == ContextStatus.RUNNING }
                exec {
                    try {
                        responseCategory = categoryRepo.create(requestCategory)
                    }
                    catch (e: Throwable){
                        status = ContextStatus.FAILING
                        errors.add(GeneralError(code = "category-in-repo-create-error", e = e))
                    }
                }
            }

            // answer preparing
            exec(prepareResponse())
        }
    }
}