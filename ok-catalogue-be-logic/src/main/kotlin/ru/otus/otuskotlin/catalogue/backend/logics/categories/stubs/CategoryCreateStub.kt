package ru.otus.otuskotlin.catalogue.backend.logics.categories.stubs

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc

val categoryCreateStub = corProc<CategoryContext> {
    isMatchable { stubCCreateCase != CategoryCreateStubCases.NONE }
    handler {
        isMatchable {stubCCreateCase == CategoryCreateStubCases.SUCCESS }
        exec {
            responseCategory = requestCategory.copy(
                    id = "stub-create-category"
            )
            status = ContextStatus.FINISHING
        }
    }
}