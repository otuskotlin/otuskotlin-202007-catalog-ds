package ru.otus.otuskotlin.catalogue.backend.logics.items

import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.errors.GeneralError
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemDeleteStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.prepareResponse
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.setRepoByWorkMode
import ru.otus.otuskotlin.catalogue.backend.logics.items.stubs.itemDeleteStub

class ItemDeleteChain(
    private val categoryRepoTest: ICategoryRepository,
    private val categoryRepoProd: ICategoryRepository
) {
    suspend fun exec(context: ItemContext) = chain.exec(context.apply {
        categoryRepoTest = this@ItemDeleteChain.categoryRepoTest
        categoryRepoProd = this@ItemDeleteChain.categoryRepoProd
    })

    companion object{
        private val chain = corProc<ItemContext> {
            // pipeline init
            exec { status = ContextStatus.RUNNING }

            // set repo in context
            processor {
                exec(setRepoByWorkMode())
            }

            // stub handling
            exec(itemDeleteStub)

            // job with db
            handler {
                isMatchable { status == ContextStatus.RUNNING }
                exec {
                    try {
                        responseItem = categoryRepo.deleteItem(requestItemId, requestCategoryId)
                    }
                    catch (e: Throwable){
                        status = ContextStatus.FAILING
                        errors.add(GeneralError(code = "item-in-repo-remove-error", e = e))
                    }
                }
            }

            // answer preparing
            exec(prepareResponse())
        }
    }
}