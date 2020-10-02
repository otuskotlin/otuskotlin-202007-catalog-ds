package ru.otus.otuskotlin.catalogue.backend.logics.categories

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import java.time.LocalDate

class CategoryCreateChain {

    suspend fun exec(ctx: CategoryContext) = chain.exec(ctx.apply {  })

    companion object{
        private val chain = corProc<CategoryContext>{
            // pipeline init
            exec { status = CategoryContextStatus.RUNNING }

            // stub handling
            processor {
                isMatchable { stubCCreateCase != CategoryCreateStubCases.NONE }

                handler {
                    isMatchable { stubCCreateCase == CategoryCreateStubCases.SUCCESS }

                    exec {
                        responseCategory = CategoryModel(
                            id = "12345",
                            label = "Notes",
                            type = "notes",
                            children = mutableSetOf(CategoryModel(id = "12346", label = "Subdir")),
                            items = mutableSetOf(
                                NoteModel(
                                id = "12",
                                header = "My note",
                                description = "Some note",
                                preview = "qwerty"
                            )),
                            creationDate = LocalDate.of(2010, 6, 13)
                        )
                        status = CategoryContextStatus.FINISHING
                    }
                }
            }

            //TODO: add validation and db logic

            // answer preparing
            exec {
                status = CategoryContextStatus.SUCCESS
            }
        }
    }
}