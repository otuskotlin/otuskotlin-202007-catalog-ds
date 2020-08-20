package ru.otus.otuskotlin.catalogue.multiplatform.common.validators


class ValidationResult(
        val errorList: List<HandleError> = emptyList()
) {
    constructor(vararg errors: HandleError?):this(errors.filterNotNull())

    val error:HandleError?
        get() = errorList.maxBy { it.level }

    val isOK:Boolean
        get() = error?.level != HandleError.Level.ERROR
}