package ru.otus.otuskotlin.catalogue.backend.logics.categories

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryDeleteStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import java.time.LocalDate

class CategoryDeleteChain {

    suspend fun exec(ctx: CategoryContext) = chain.exec(ctx.apply {  })

    companion object{
        private val chain = corProc<CategoryContext>{
            // pipeline init
            exec { status = ContextStatus.RUNNING }

            // stub handling
            processor {
                isMatchable { stubCDeleteCase != CategoryDeleteStubCases.NONE }

                handler {
                    isMatchable { stubCDeleteCase == CategoryDeleteStubCases.SUCCESS }

                    exec {
                        responseCategory = CategoryModel(
                            id = "stub-delete-category",
                            label = "Notes",
                            type = "notes",
                            children = mutableSetOf(CategoryModel(id = requestCategoryId, label = "Subdir")),
                            items = mutableSetOf(
                                NoteModel(
                                    id = "12",
                                    header = "My note",
                                    description = "Some note",
                                    preview = "qwerty"
                                )
                            ),
                            creationDate = LocalDate.of(2010, 6, 13)
                        ).apply { children.removeIf { it.id == requestCategoryId } }
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