package ru.otus.otuskotlin.catalogue.transport.common.validators.rules

import ru.otus.otuskotlin.catalogue.transport.common.validators.HandleError
import ru.otus.otuskotlin.catalogue.transport.common.validators.IValidator
import ru.otus.otuskotlin.catalogue.transport.common.validators.ValidationResult

class ValidatorRegex(
    private val regex: Regex,
    private val code: String = "Wrong symbols",
    private val message: String = "Field contains invalid chars.",
    private val level: HandleError.Level = HandleError.Level.ERROR
): IValidator<String> {
    override fun validate(arg: String): ValidationResult {
        regex.replace(arg, "").let {
            if (it.isEmpty()){
                val error = HandleError(code = code,
                                        message = message,
                                        level = level)
                return ValidationResult(error)
            }
        }
        return ValidationResult(HandleError())
    }
}