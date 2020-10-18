package ru.otus.otuskotlin.catalogue.backend.common.validators

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ValidatorProcTest {

    data class SomeData(
        var id: String = "",
        var name: String = "",
        var date: String = "",
        var errors: MutableList<SomeError> = mutableListOf()
    )

    class SomeError(
        var code: String = "some-code",
        var message: String = "error message"
    )

    @Test
    fun testCreateValidator(){
        val data = SomeData()

        runBlocking {
            data.errors addRange validator<SomeData, SomeError> {
                stopOnError = true

                validate {
                    if (id.isBlank())
                        return@validate SomeError(message = "id is blank")
                    else
                        return@validate null
                }
                validate {
                    if (name.length < 3)
                        return@validate SomeError(message = "name is too short")
                    else
                        return@validate null
                }

            }.validate(data)
        }
        data.errors.forEach {
            println("error: ${it.code} ${it.message}")
        }
        assertEquals(1, data.errors.size)

    }
}