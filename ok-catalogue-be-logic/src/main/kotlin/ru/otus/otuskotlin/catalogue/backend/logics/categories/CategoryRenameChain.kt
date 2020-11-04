package ru.otus.otuskotlin.catalogue.backend.logics.categories

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.errors.GeneralError
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryRenameStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.prepareResponse
import ru.otus.otuskotlin.catalogue.backend.logics.handlers.setRepoByWorkMode
import java.time.LocalDate

class CategoryRenameChain(
    private val categoryRepoTest: ICategoryRepository,
    private val categoryRepoProd: ICategoryRepository
) {

    suspend fun exec(ctx: CategoryContext) = chain.exec(ctx.apply {
        categoryRepoTest = this@CategoryRenameChain.categoryRepoTest
        categoryRepoProd = this@CategoryRenameChain.categoryRepoProd
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
                isMatchable {
                    stubCRenameCase != CategoryRenameStubCases.NONE
                }

                handler {
                    isMatchable { stubCRenameCase == CategoryRenameStubCases.SUCCESS }

                    exec {
                        responseCategory = CategoryModel(
                            id = requestCategoryId,
                            label = requestLabel,
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

            // job with db
            handler {
                isMatchable { status == ContextStatus.RUNNING }
                exec {
                    try {
                        responseCategory = categoryRepo.rename(id = requestCategoryId, label = requestLabel)
                    }
                    catch (e: Throwable){
                        status = ContextStatus.FAILING
                        errors.add(GeneralError(code = "category-in-repo-rename-error", e = e))
                    }
                }
            }

            // answer preparing
            exec(prepareResponse())
        }
    }
}