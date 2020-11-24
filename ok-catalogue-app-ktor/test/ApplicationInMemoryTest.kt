package ru.otus.otuskotlin

import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.junit.BeforeClass
import ru.otus.otuskotlin.catalogue.transport.common.models.CatalogueDbModes
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.queries.*
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses.CategoryGetMapResponse
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses.CategoryGetResponse
import ru.otus.otuskotlin.catalogue.transport.common.models.items.*
import java.util.*
import kotlin.test.*

internal class ApplicationInMemoryTest {
    companion object {
        @KtorExperimentalAPI
        private val engine = TestApplicationEngine(createTestEnvironment {
            config = HoconApplicationConfig(ConfigFactory.load())
        })

        private val format = Json {

            classDiscriminator = "#class"
            prettyPrint = true
        }

        @KtorExperimentalAPI
        @BeforeClass
        @JvmStatic fun setup(){
            engine.start(wait = false)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun testRoot(){
        with(engine){
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertTrue {
                    response.content?.contains("HELLO WORLD!") ?: false
                }
            }
        }
    }

    @KtorExperimentalAPI
    @Test
    fun createCategoryTest(){
        with(engine){
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    label = "test-create",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                println(bodyString)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                assertFalse { res.data?.id.isNullOrBlank() }
            }
        }
    }

    @KtorExperimentalAPI
    @Test
    fun getCategoryTest(){
        with(engine){
            var id = ""
            var subId = ""
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    label = "test-get",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)
                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                id = res.data?.id?: fail("No id data in create response")
            }
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    parentId = id,
                    label = "test-get-sub",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)
                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                subId = res.data?.id?: fail("No id data in create response")
            }
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    parentId = subId,
                    label = "test-get-sub-sub",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)
                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                subId = res.data?.id?: fail("No id data in create response")
            }
            handleRequest(HttpMethod.Post, "/catalogue/get"){
                val body = CategoryGetQuery(
                   categoryId = id,
                    debug = CategoryGetQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(CategoryGetQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                assertEquals("test-get", res.data?.label?:"")
                assertEquals(1, res.data?.childList?.size?:0)
            }
            handleRequest(HttpMethod.Post, "/catalogue/get"){
                val body = CategoryGetQuery(
                    categoryId = subId,
                    debug = CategoryGetQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(CategoryGetQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                assertEquals(2, res.data?.parentList?.size)
            }
        }
    }

    @KtorExperimentalAPI
    @Test
    fun renameCategoryTest(){
        with(engine){
            var id = ""
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    label = "test-get",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                id = res.data?.id?: fail("No id data in create response")
            }
            handleRequest(HttpMethod.Post, "/catalogue/rename"){
                val body = CategoryRenameQuery(
                    categoryId = id,
                    modLabel = "test-rename",
                    debug = CategoryRenameQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryRenameQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                assertEquals("test-rename", res.data?.label?:"")
            }
            handleRequest(HttpMethod.Post, "/catalogue/get"){
                val body = CategoryGetQuery(
                    categoryId = id,
                    debug = CategoryGetQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(CategoryGetQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                assertEquals("test-rename", res.data?.label?:"")
            }
        }
    }

    @KtorExperimentalAPI
    @Test
    fun deleteCategoryTest(){
        with(engine){
            var id = ""
            var subId = ""
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    label = "test-delete",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                id = res.data?.id?: fail("No id data in create response")
            }
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    parentId = id,
                    label = "test-delete-sub",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                subId = res.data?.id?: fail("No id data in create response")
            }
            handleRequest(HttpMethod.Post, "/catalogue/delete"){
                val body = CategoryDeleteQuery(
                    categoryId = subId,
                    debug = CategoryDeleteQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryDeleteQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                assertEquals(subId, res.data?.id)
            }
            handleRequest(HttpMethod.Post, "/catalogue/get"){
                val body = CategoryGetQuery(
                    categoryId = id,
                    debug = CategoryGetQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(CategoryGetQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                assertEquals(0, res.data?.childList?.size)
            }
        }
    }

    @KtorExperimentalAPI
    @Test
    fun getMapCategoryTest(){
        with(engine){
            var id = ""
            var subId = ""
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    label = "test-get",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)
                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                id = res.data?.id?: fail("No id data in create response")
            }
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    parentId = id,
                    label = "test-get-sub",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)
                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                subId = res.data?.id?: fail("No id data in create response")
            }

            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    parentId = subId,
                    label = "test-get-sub-sub",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }

            handleRequest(HttpMethod.Post, "/catalogue/map"){
                val body = CategoryGetMapQuery(
                    id = id,
                    debug = CategoryGetMapQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(CategoryGetMapQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetMapResponse.serializer(), jsonString)
                assertEquals(1, res.data?.children?.size?:0)
                assertEquals(1, res.data?.children?.find { it.id == subId }?.children?.size?:0)
            }

        }
    }

    @KtorExperimentalAPI
    @Test
    fun addItemTest(){
        with(engine){
            var id = ""
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    label = "test-get",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)
                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                id = res.data?.id?: fail("No id data in create response")
            }

            handleRequest(HttpMethod.Post, "/catalogue/addItem"){
                val body = NoteCreateQuery(
                    id = UUID.randomUUID().toString(),
                    categoryId = id,
                    header = "add-note",
                    debug = ItemCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(ItemCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                val res = format.decodeFromString(ItemResponse.serializer(), jsonString)
                assertEquals(NoteInfo::class, res.data!!::class)
                assertEquals("add-note", (res.data as NoteInfo).header)
            }

            handleRequest(HttpMethod.Post, "/catalogue/get"){
                val body = CategoryGetQuery(
                    categoryId = id,
                    debug = CategoryGetQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(CategoryGetQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                assertEquals(1, res.data?.itemList?.size)
            }
        }
    }

    @KtorExperimentalAPI
    @Test
    fun deleteItemTest(){
        with(engine){
            var id = ""
            var itemId = ""
            handleRequest(HttpMethod.Post, "/catalogue/create"){
                val body = CategoryCreateQuery(
                    label = "test-get",
                    type = "notes",
                    debug = CategoryCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val  bodyString = format.encodeToString(CategoryCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)
                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                id = res.data?.id?: fail("No id data in create response")
            }

            handleRequest(HttpMethod.Post, "/catalogue/addItem"){
                val body = NoteCreateQuery(
                    id = UUID.randomUUID().toString(),
                    categoryId = id,
                    header = "add-note",
                    debug = ItemCreateQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(ItemCreateQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                val res = format.decodeFromString(ItemResponse.serializer(), jsonString)
                itemId = (res.data as NoteInfo).id ?:""
            }

            handleRequest(HttpMethod.Post, "/catalogue/delItem"){
                val body = ItemDeleteQuery(
                    itemId = itemId,
                    categoryId = id,
                    debug = ItemDeleteQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(ItemDeleteQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                val res = format.decodeFromString(ItemResponse.serializer(), jsonString)
                assertEquals(itemId, (res.data as NoteInfo).id)
            }

            handleRequest(HttpMethod.Post, "/catalogue/get"){
                val body = CategoryGetQuery(
                    categoryId = id,
                    debug = CategoryGetQuery.Debug(dbMode = CatalogueDbModes.TEST)
                )
                val bodyString = format.encodeToString(CategoryGetQuery.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = format.decodeFromString(CategoryGetResponse.serializer(), jsonString)
                assertEquals(0, res.data?.itemList?.size)
            }
        }
    }
}