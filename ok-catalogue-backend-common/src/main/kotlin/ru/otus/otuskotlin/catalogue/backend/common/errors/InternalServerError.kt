package ru.otus.otuskotlin.catalogue.backend.common.errors

import ru.otus.otuskotlin.catalogue.backend.common.models.IErrorModel

data class InternalServerError (
        override val code: String = "internal-error",
        override val group: IErrorModel.Groups = IErrorModel.Groups.SERVER,
        override val field: String = "",
        override val level: IErrorModel.Levels = IErrorModel.Levels.ERROR,
        override val message: String = "Internal server error. If it continues to rise, please, apply to the Administrator"
) : IErrorModel {
    companion object {
        val instance = InternalServerError()
    }
}