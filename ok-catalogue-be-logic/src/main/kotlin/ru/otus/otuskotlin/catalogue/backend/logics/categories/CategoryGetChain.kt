package ru.otus.otuskotlin.catalogue.backend.logics.categories

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryGetStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.common.validators.addRange
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import ru.otus.otuskotlin.catalogue.backend.logics.validators.fields.IdValidator
import java.time.LocalDate

class CategoryGetChain {

    suspend fun exec(ctx: CategoryContext) = chain.exec(ctx.apply {  })

    companion object{
        private val chain = corProc<CategoryContext>{
            // pipeline init
            exec { status = ContextStatus.RUNNING }

            // stub handling
            processor {
                isMatchable { stubCGetCase != CategoryGetStubCases.NONE }

                handler {
                    isMatchable { stubCGetCase == CategoryGetStubCases.SUCCESS }

                    exec {
                        responseCategory = CategoryModel(
                            id = requestCategoryId,
                            label = "Notes",
                            type = "notes",
                            children = mutableSetOf(CategoryModel(id = "12346", label = "Subdir")),
                            items = mutableSetOf(
                                NoteModel(
                                    id = "12",
                                    header = "My note",
                                    description = "Some note",
                                    preview = "qwerty"
                                )
                            ),
                            creationDate = LocalDate.of(2010, 6, 13)
                        )
                        status = ContextStatus.FINISHING
                    }
                }
            }

            //TODO: add validation and db logic
            processor {
                isMatchable { status != ContextStatus.FINISHING }
                exec {
                    errors addRange IdValidator().validate(requestCategoryId)
                    if (errors.any { it.level.isError })
                        status = ContextStatus.FAILING
                }
            }

            // answer preparing
            exec {
                status = ContextStatus.SUCCESS
            }
        }
    }
}