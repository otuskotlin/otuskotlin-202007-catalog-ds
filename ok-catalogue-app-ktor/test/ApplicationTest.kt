package ru.otus.otuskotlin


import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryDTO
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryGetQuery
import kotlin.test.assertEquals
import kotlin.test.fail

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    @Test
    fun testGet(){
        withTestApplication({ module(testing = true)}){
            handleRequest(HttpMethod.Post, uri = "/catalogue/get"){
                val body = CategoryGetQuery(
                        categoryId = "12345"
                )
                val  bodyString = Json.encodeToString(CategoryGetQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                val res = Json.decodeFromString(CategoryDTO.serializer(), jsonString)
                assertEquals("12345", res.id)
            }
        }
    }
}
