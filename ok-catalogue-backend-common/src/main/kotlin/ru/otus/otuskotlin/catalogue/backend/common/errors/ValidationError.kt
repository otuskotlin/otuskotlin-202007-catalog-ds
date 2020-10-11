package ru.otus.otuskotlin.catalogue.backend.common.errors

import ru.otus.otuskotlin.catalogue.backend.common.models.IErrorModel

data class ValidationError(
    override val code: String = "validation-error",
    override val group: IErrorModel.Groups = IErrorModel.Groups.VALIDATION,
    override val field: String = "",
    override val level: IErrorModel.Levels = IErrorModel.Levels.ERROR,
    override val message: String = "Invalidate value in $field field."

): IErrorModel {
    companion object{
        val instance = ValidationError()
    }
}