package ru.otus.otuskotlin.catalogue.backend.logics.categories

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.errors.GeneralError
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryGetStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import ru.otus.otuskotlin.catalogue.backend.common.validators.addRange
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.prepareResponse
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.setRepoByWorkMode
import ru.otus.otuskotlin.catalogue.backend.logics.validators.fields.IdValidator
import java.time.LocalDate

class CategoryGetChain(
    private val categoryRepoTest: ICategoryRepository,
    private val categoryRepoProd: ICategoryRepository
) {

    suspend fun exec(ctx: CategoryContext) = chain.exec(ctx.apply {
        categoryRepoProd = this@CategoryGetChain.categoryRepoProd
        categoryRepoTest = this@CategoryGetChain.categoryRepoTest
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
                isMatchable { status == ContextStatus.RUNNING }
                exec {
                    errors addRange IdValidator().validate(requestCategoryId)
                    if (errors.any { it.level.isError })
                        status = ContextStatus.FAILING
                }
            }

            // job with db
            handler {
                isMatchable { status == ContextStatus.RUNNING }
                exec {
                    try {
                        responseCategory = categoryRepo.get(requestCategoryId)
                    }
                    catch (e: Throwable){
                        status = ContextStatus.FAILING
                        errors.add(GeneralError(code = "category-in-repo-get-error", e = e))
                    }
                }
            }

            // answer preparing
            exec(prepareResponse())
        }
    }
}