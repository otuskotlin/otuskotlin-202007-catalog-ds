package ru.otus.otuskotlin.catalogue.backend.logics.items.stubs

import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemDeleteStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc

val itemDeleteStub = corProc<ItemContext> {
    isMatchable { stubIDeleteCase != ItemDeleteStubCases.NONE }
    handler {
        isMatchable { stubIDeleteCase == ItemDeleteStubCases.SUCCESS }
        exec {
            responseItem = NoteModel(
                    id = requestItemId,
                    categoryId = requestCategoryId,
                    header = "stub-item"
            )
            status = ContextStatus.FINISHING
        }
    }
}