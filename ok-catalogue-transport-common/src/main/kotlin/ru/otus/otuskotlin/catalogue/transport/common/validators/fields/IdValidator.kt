package ru.otus.otuskotlin.catalogue.transport.common.validators.fields

import ru.otus.otuskotlin.catalogue.transport.common.validators.HandleError
import ru.otus.otuskotlin.catalogue.transport.common.validators.IValidator
import ru.otus.otuskotlin.catalogue.transport.common.validators.ValidationResult
import ru.otus.otuskotlin.catalogue.transport.common.validators.rules.IsEmptyValidator
import ru.otus.otuskotlin.catalogue.transport.common.validators.rules.ValidatorRegex

class IdValidator: IValidator<String> {
    override fun validate(arg: String): ValidationResult {
        val result = ValidationResult()
        if(!(result.add(ValidatorRegex(regex = Regex("[0-9]")).validate(arg).error).isOK))
            return result
        return result.add(IsEmptyValidator(level = HandleError.Level.WARNING).validate(arg).error)
    }
}