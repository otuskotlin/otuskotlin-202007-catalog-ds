package ru.otus.otuskotlin.catalogue.backend.logics.items

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc

class ItemCreateChain {

    suspend fun exec(context: ItemContext) = chain.exec(context.apply {  })

    companion object{
        private val chain = corProc<ItemContext> {
            // pipeline init
            exec { status = ContextStatus.RUNNING }

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

            //TODO: add validation and db logic

            // answer preparing
            exec {
                status = ContextStatus.SUCCESS
            }
        }
    }
}