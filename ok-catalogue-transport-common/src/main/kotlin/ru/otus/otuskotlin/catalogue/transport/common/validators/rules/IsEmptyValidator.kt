package ru.otus.otuskotlin.catalogue.transport.common.validators.rules

import ru.otus.otuskotlin.catalogue.transport.common.validators.HandleError
import ru.otus.otuskotlin.catalogue.transport.common.validators.IValidator
import ru.otus.otuskotlin.catalogue.transport.common.validators.ValidationResult

class IsEmptyValidator(
    private val code: String = "Empty field",
    private val message:String = "One field is empty.",
    private val level: HandleError.Level = HandleError.Level.ERROR
): IValidator<String> {
    override fun validate(arg: String): ValidationResult =
        if(arg.isEmpty())
            ValidationResult(
                HandleError(
                    code = code,
                    message = message,
                    level = level
            ))
        else ValidationResult()

}