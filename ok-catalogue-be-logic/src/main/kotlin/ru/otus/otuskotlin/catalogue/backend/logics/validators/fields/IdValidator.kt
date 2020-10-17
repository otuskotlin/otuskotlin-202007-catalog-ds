package ru.otus.otuskotlin.catalogue.backend.logics.validators.fields

import ru.otus.otuskotlin.catalogue.backend.common.errors.ValidationError
import ru.otus.otuskotlin.catalogue.backend.common.models.IErrorModel
import ru.otus.otuskotlin.catalogue.backend.common.validators.validator
import java.util.*

class IdValidator {

    suspend fun validate(id: String): MutableList<IErrorModel> = validator.validate(id)

    companion object{
        private val FIELD = "ID"

        private val validator = validator<String, IErrorModel> {
            stopOnError = true

            validate {
                if (this.isBlank())
                    return@validate ValidationError(
                                    field = FIELD,
                                    message = "ID must not be blank.")
                else return@validate null
            }

            validate {
                try {
                        UUID.fromString(this)
                        return@validate null
                    }
                catch (e: IllegalArgumentException){
                    return@validate ValidationError(
                        field = FIELD,
                        message = "ID must be UUID."
                    )
                }
            }
        }
    }
}