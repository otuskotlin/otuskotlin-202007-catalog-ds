package ru.otus.otuskotlin.catalogue.backend.common.errors

import ru.otus.otuskotlin.catalogue.backend.common.models.IErrorModel

data class GeneralError(
    override val code: String = "",
    override val group: IErrorModel.Groups,
    override val field: String = "",
    override val level: IErrorModel.Levels = IErrorModel.Levels.ERROR,
    override val message: String = ""
) : IErrorModel {
    constructor(
        code: String,
        group: IErrorModel.Groups = IErrorModel.Groups.SERVER,
        e: Throwable
    ): this(
        code = code,
        group = group,
        message = e.message ?: "Unknown exception"
    )
}