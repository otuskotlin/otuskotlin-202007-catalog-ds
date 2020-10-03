package ru.otus.otuskotlin.catalogue.backend.logics.items

import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemDeleteStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import java.time.LocalDate

class ItemDeleteChain {
    suspend fun exec(context: ItemContext) = chain.exec(context.apply {  })

    companion object{
        private val chain = corProc<ItemContext> {
            // pipeline init
            exec { status = ContextStatus.RUNNING }

            // stub handling
            processor {
                isMatchable {
                    stubIDeleteCase != ItemDeleteStubCases.NONE
                }

                handler {
                    isMatchable {
                        stubIDeleteCase == ItemDeleteStubCases.SUCCESS
                    }

                    exec {
                        responseItem = NoteModel()
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