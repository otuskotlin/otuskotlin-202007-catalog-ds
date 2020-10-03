package ru.otus.otuskotlin.catalogue.backend.common.contexts

import ru.otus.otuskotlin.catalogue.backend.common.models.IErrorModel

abstract class BaseContext (
        var errors: MutableList<IErrorModel> = mutableListOf(),
        var status: ContextStatus = ContextStatus.NONE
)

