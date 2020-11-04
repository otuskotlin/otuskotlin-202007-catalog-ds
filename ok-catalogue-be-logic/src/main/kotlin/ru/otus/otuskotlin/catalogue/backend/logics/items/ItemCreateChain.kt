package ru.otus.otuskotlin.catalogue.backend.logics.items

import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.errors.GeneralError
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.prepareResponse
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.setRepoByWorkMode

class ItemCreateChain(
    private val categoryRepoTest: ICategoryRepository,
    private val categoryRepoProd: ICategoryRepository
) {

    suspend fun exec(context: ItemContext) = chain.exec(context.apply {
        categoryRepoTest = this@ItemCreateChain.categoryRepoTest
        categoryRepoProd = this@ItemCreateChain.categoryRepoProd
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
            processor {
                isMatchable {
                    stubICreateCase != ItemCreateStubCases.NONE
                }

                handler {
                    isMatchable {
                        stubICreateCase == ItemCreateStubCases.SUCCESS
                    }

                    exec {
                        responseItem = requestItem
                        status = ContextStatus.FINISHING
                    }
                }
            }

            // job with db
            handler {
                isMatchable { status == ContextStatus.RUNNING }
                exec {
                    try {
                        responseItem = categoryRepo.addItem(requestItem)
                    }
                    catch (e: Throwable){
                        status = ContextStatus.FAILING
                        errors.add(GeneralError(code = "item-in-repo-add-error", e = e))
                    }
                }
            }

            // answer preparing
            exec(prepareResponse())
        }
    }
}