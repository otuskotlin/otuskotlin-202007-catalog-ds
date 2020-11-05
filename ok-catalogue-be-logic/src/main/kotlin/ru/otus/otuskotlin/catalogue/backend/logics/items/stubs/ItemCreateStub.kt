package ru.otus.otuskotlin.catalogue.backend.logics.items.stubs

import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc

val itemCreateStub = corProc<ItemContext> {
    isMatchable { stubICreateCase != ItemCreateStubCases.NONE }
    handler {
        isMatchable { stubICreateCase == ItemCreateStubCases.SUCCESS }
        exec {
            responseItem = requestItem
            status = ContextStatus.FINISHING
        }
    }
}