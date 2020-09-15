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
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.*
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.rest.service.CategoryService
import ru.otus.otuskotlin.catalogue.transport.rest.service.ItemService

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val service = CategoryService()
    val itemService = ItemService()

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

        route("/catalogue"){
            post("/get") {
                val query = call.receive<CategoryGetQuery>()
                call.respond(service.get(query))
            }
            post("/create") {
                val query = call.receive<CategoryCreateQuery>()
                call.respond(service.create(query))
            }
            post("/delete") {
                val query = call.receive<CategoryDeleteQuery>()
                call.respond(service.delete(query))
            }
            post("/rename") {
                val query = call.receive<CategoryRenameQuery>()
                call.respond(service.rename(query))
            }
            post("/map") {
                val query = call.receive<CategoryGetMapQuery>()
                call.respond(service.map(query))
            }
            post ("/addItem" ){
                val query = call.receive<ItemCreateQuery>()
                call.respond(itemService.addItem(query))
            }
            post("/delItem"){
                val query = call.receive<ItemDeleteQuery>()
                call.respond(itemService.delItem(query))
            }

        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }
    }
}

