package ru.otus.otuskotlin.catalogue.backend.logics.categories

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc

class CategoryCreateChain {

    suspend fun exec(ctx: CategoryContext) = chain.exec(ctx.apply {  })

    companion object{
        private val chain = corProc<CategoryContext>{
            // pipeline init
            exec { status = ContextStatus.RUNNING }

            // stub handling
            processor {
                isMatchable { stubCCreateCase != CategoryCreateStubCases.NONE }

                handler {
                    isMatchable { stubCCreateCase == CategoryCreateStubCases.SUCCESS }

                    exec {
                        responseCategory = requestCategory.copy(
                            id = "stub-create-category"
                        )
                        status = ContextStatus.FINISHING
                    }
                }
            }

            processor {
                isMatchable { status != ContextStatus.FINISHING }
                exec {

                }
            }

            //TODO: add validation and db logic

            // answer preparing
            exec {
                status = ContextStatus.SUCCESS
            }
        }
    }
}