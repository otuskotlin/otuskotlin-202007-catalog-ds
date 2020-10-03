package ru.otus.otuskotlin


import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.*
import ru.otus.otuskotlin.catalogue.transport.common.models.items.*
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
                println(jsonString)

                // Need to use abstract class instead of sealed
                val format = Json {
                    classDiscriminator = "#class"
                    prettyPrint = true
                }
                //

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)

                assertEquals("12345", res.data?.id)
            }
        }
    }

    @Test
    fun testCreate(){
        withTestApplication({ module(testing = true)}){
            handleRequest(HttpMethod.Post, uri = "/catalogue/create"){
                val body = CategoryCreateQuery(
                    parentId = "123",
                    type = "Note",
                    label = "Recent"
                )
                val  bodyString = Json.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                // Need to use abstract class instead of sealed
                val format = Json {
                    classDiscriminator = "#class"
                    prettyPrint = true
                }
                //

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)

                assertEquals("asdf", res.data?.id)
            }
        }
    }

    @Test
    fun testDelete() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, uri = "/catalogue/delete") {
                val body = CategoryDeleteQuery(
                   categoryId = "12346"
                )
                val bodyString = Json.encodeToString(CategoryDeleteQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                // Need to use abstract class instead of sealed
                val format = Json {
                    classDiscriminator = "#class"
                    prettyPrint = true
                }
                //

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)

                assertEquals(emptyList(), res.data?.childList)
            }
        }
    }

    @Test
    fun testRename() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, uri = "/catalogue/rename") {
                val body = CategoryRenameQuery(
                    categoryId = "12345",
                    modLabel = "New label"
                )
                val bodyString = Json.encodeToString(CategoryRenameQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                // Need to use abstract class instead of sealed
                val format = Json {
                    classDiscriminator = "#class"
                    prettyPrint = true
                }
                //

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)

                assertEquals("New label", res.data?.label)
            }
        }
    }

    @Test
    fun testMap() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, uri = "/catalogue/map") {
                val body = CategoryGetMapQuery(
                    id = "12345"
                )
                val bodyString = Json.encodeToString(CategoryGetMapQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                // Need to use abstract class instead of sealed
                val format = Json {
                    serializersModule = SerializersModule {
                        polymorphic(
                            baseClass = ItemInfo::class,
                            actualClass = NoteInfo::class,
                            actualSerializer = NoteInfo.serializer()
                        )
                    }
                    prettyPrint = true
                }
                //

                val res = format.decodeFromString(CategoryGetMapResponse.serializer(), jsonString)

                assertEquals("12345", res.data?.id)
            }
        }
    }

    @Test
    fun testSerialization(){
        val body = NoteCreateQuery(
            categoryId = "12345",
            id = "54321",
            preview = "Some text here..."
        )
        val json = Json {

                             classDiscriminator = "#class"
            prettyPrint = true
        }

        val bodyString = json.encodeToString(ItemCreateQuery.serializer(), body)
        println(bodyString)
        assertTrue(bodyString.isNotEmpty())
        val model = json.decodeFromString(ItemCreateQuery.serializer(), bodyString)
        println(model)
        assertEquals(true, model is NoteCreateQuery)
    }

    @Test
    fun testAddItem() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, uri = "/catalogue/addItem") {
                val body = NoteCreateQuery(
                    categoryId = "12345",
                    id = "54321",
                    preview = "Some text here..."
                )
                val json = Json {

                    classDiscriminator = "#class"
                    prettyPrint = true
                }
                val bodyString = json.encodeToString(ItemCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                // Need to use abstract class instead of sealed
                val format = Json {
//                    serializersModule = SerializersModule {
//                        polymorphic(
//                            baseClass = ItemInfo::class,
//                            actualClass = NoteInfo::class,
//                            actualSerializer = NoteInfo.serializer()
//                        )
//                    }
                    classDiscriminator = "#class"
                    prettyPrint = true
                }
                //

                val res = format.decodeFromString(ItemResponse.serializer(), jsonString)

                assertEquals("54321", (res.data as NoteInfo).id)
            }
        }
    }

    @Test
    fun testDeleteItem() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, uri = "/catalogue/delItem") {
                val body = ItemDeleteQuery(
                    categoryId = "12345",
                    itemId = "12"
                )

                val bodyString = Json.encodeToString(ItemDeleteQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                // Need to use abstract class instead of sealed
//                val format = Json {
//                    serializersModule = SerializersModule {
//                        polymorphic(
//                            baseClass = ItemInfo::class,
//                            actualClass = NoteInfo::class,
//                            actualSerializer = NoteInfo.serializer()
//                        )
//                    }
//                    prettyPrint = true
//                }
                //

                val res = Json.decodeFromString(CategoryGetResponse.serializer(), jsonString)

                assertEquals(true, res.data?.itemList?.isEmpty())
            }
        }
    }
}
