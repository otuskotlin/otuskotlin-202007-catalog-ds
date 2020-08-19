package ru.otus.otuskotlin.catalogue.multiplatform.transport.validators

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class IValidatorTest {

    @Test
    fun idValidateTest(){
        val validator = IdValidator()

        val negative = validator.validate(-1)
        assertEquals(HandleError.Level.ERROR, negative.error?.level)
        assertEquals("Id must be positive", negative.error?.message)
        assertFalse(negative.isOK)

    }

    class IdValidator : IValidator<Int> {
        override fun validate(arg: Int): ValidationResult = ValidationResult(
                if(arg < 0) HandleError(code = "negative", level = HandleError.Level.ERROR, message = "Id must be positive") else null,
                if (arg > 65000) HandleError(code = "too big", level = HandleError.Level.WARNING, message = "Id must be less than 65000") else null,
                if (arg in 0..65000) HandleError(code = "correct", level = HandleError.Level.NONE, message = "Id is validate") else null
        )

    }
}