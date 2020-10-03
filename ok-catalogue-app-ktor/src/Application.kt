package ru.otus.otuskotlin

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.features.*
import io.ktor.serialization.json
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.catalogue.backend.logics.categories.CategoryCrud
import ru.otus.otuskotlin.catalogue.backend.logics.items.ItemCrud
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.queries.*
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.rest.service.CategoryService
import ru.otus.otuskotlin.catalogue.transport.rest.service.ItemService

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val service = CategoryService(CategoryCrud.DEFAULT)
    val itemService = ItemService(ItemCrud.DEFAULT)

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ContentNegotiation) {
        json(

                contentType = ContentType.Application.Json,
                json = Json {

                    classDiscriminator = "#class"
                    prettyPrint = true
                }
        )
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        route("/catalogue") {
            post("/get") {
                val query = call.receiveOrNull<CategoryGetQuery>()
                query?.let {
                    call.respond(service.get(query))
                } ?: call.respond(HttpStatusCode.BadGateway)
            }
            post("/create") {
                val query = call.receiveOrNull<CategoryCreateQuery>()
                query?.let {
                    call.respond(service.create(query))
                } ?: call.respond(HttpStatusCode.BadGateway)
            }
            post("/delete") {
                val query = call.receiveOrNull<CategoryDeleteQuery>()
                query?.let {
                    call.respond(service.delete(query))
                } ?: call.respond(HttpStatusCode.BadGateway)
            }
            post("/rename") {
                val query = call.receiveOrNull<CategoryRenameQuery>()
                query?.let {
                    call.respond(service.rename(query))
                } ?: call.respond(HttpStatusCode.BadGateway)
            }
            post("/map") {
                val query = call.receiveOrNull<CategoryGetMapQuery>()
                query?.let {
                    call.respond(service.getMap(query))
                }?:call.respond(HttpStatusCode.BadGateway)
            }
            post("/addItem") {
                val query = call.receiveOrNull<ItemCreateQuery>()
                query?.let {
                    call.respond(itemService.addItem(query))
                }?:call.respond(HttpStatusCode.BadGateway)
            }
            post("/delItem") {
                val query = call.receiveOrNull<ItemDeleteQuery>()
                query?.let {
                    call.respond(itemService.delItem(query))
                }?:call.respond(HttpStatusCode.BadGateway)
            }

        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }
    }
}

