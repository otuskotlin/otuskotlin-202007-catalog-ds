package ru.otus.otuskotlin.catalogue.backend.logics.categories.stubs

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryRenameStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import java.time.LocalDate

val categoryRenameStub = corProc<CategoryContext> {
    isMatchable { stubCRenameCase != CategoryRenameStubCases.NONE }
    handler {
        isMatchable { stubCRenameCase == CategoryRenameStubCases.SUCCESS }
        exec {
            responseCategory = CategoryModel(
                    id = requestCategoryId,
                    label = requestLabel,
                    type = "notes",
                    items = mutableSetOf(NoteModel(categoryId = requestCategoryId, id = "test-item", header = "some-note")),
                    children = mutableSetOf(CategoryModel(id = "test-category", parentId = requestCategoryId, label = "subdir")),
                    creationDate = LocalDate.of(2010, 6, 13)
            )
            status = ContextStatus.FINISHING
        }
    }
}