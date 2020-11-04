package ru.otus.otuskotlin.catalogue.backend.logics.handlers

import ru.otus.otuskotlin.catalogue.backend.common.contexts.BaseContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc

inline fun <reified T: BaseContext> prepareResponse() = corProc<T> {
    handler {
        isMatchable { status in arrayOf(ContextStatus.RUNNING, ContextStatus.FINISHING) }
        exec {
            status = ContextStatus.SUCCESS
        }
    }

    handler {
        isMatchable { status != ContextStatus.SUCCESS }
        exec {
            status = ContextStatus.ERROR
        }
    }
}