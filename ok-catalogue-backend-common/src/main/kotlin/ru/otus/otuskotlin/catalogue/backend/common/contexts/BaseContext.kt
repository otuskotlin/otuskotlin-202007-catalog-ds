package ru.otus.otuskotlin.catalogue.backend.common.contexts

import ru.otus.otuskotlin.catalogue.backend.common.models.IErrorModel

abstract class BaseContext (
        open var errors: MutableList<IErrorModel> = mutableListOf(),
        open var status: ContextStatus = ContextStatus.NONE
)

