package ru.otus.otuskotlin.catalogue.transport.common.validators


class ValidationResult(
        val errorList: MutableList<HandleError> = mutableListOf()
) {
    constructor(vararg errors: HandleError?):this(errors.filterNotNull().toMutableList())

    val error:HandleError
        get() = errorList?.maxBy { it.level }?:HandleError()

    val isOK:Boolean
        get() = error?.level != HandleError.Level.ERROR

    fun add(error: HandleError):ValidationResult{
        errorList.add(error)
        return this
    }
}