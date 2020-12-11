package ru.otus.otuskotlin

import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.features.*
import io.ktor.serialization.json
import io.ktor.util.*
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.catalogue.backend.logics.categories.CategoryCrud
import ru.otus.otuskotlin.catalogue.backend.logics.items.ItemCrud
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryRepositoryCassandra
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteRepositoryCassandra
import ru.otus.otuskotlin.catalogue.backend.repository.inmemory.categories.CategoryRepositoryInMemory
import ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items.NoteRepositoryInMemory
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.queries.*
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.rest.service.CategoryService
import ru.otus.otuskotlin.catalogue.transport.rest.service.ItemService
import ru.otus.otuskotlin.configs.CassandraConfig
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@OptIn(ExperimentalTime::class)
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val config = HoconApplicationConfig(ConfigFactory.load())
    //TODO: try with environment
    val cassandraConfig = CassandraConfig(config)

    val itemRepoTest = NoteRepositoryInMemory(ttl = 30.toDuration(DurationUnit.MINUTES))
    val categoryRepoTest = CategoryRepositoryInMemory(ttl = 30.toDuration(DurationUnit.MINUTES))
        .addItemRepository(itemRepoTest)

    val itemRepoProd = NoteRepositoryCassandra(
        keySpace = cassandraConfig.keyspace,
        hosts = cassandraConfig.hosts,
        port = cassandraConfig.port,
        user = cassandraConfig.user,
        pass = cassandraConfig.pass
    )

    val categoryRepoProd = CategoryRepositoryCassandra(
        keySpace = cassandraConfig.keyspace,
        hosts = cassandraConfig.hosts,
        port = cassandraConfig.port,
        user = cassandraConfig.user,
        pass = cassandraConfig.pass
    ).addItemRepository(itemRepoProd)

    val itemCrud = ItemCrud(
        categoryRepoTest = categoryRepoTest,
        categoryRepoProd = categoryRepoProd
    )

    val categoryCrud = CategoryCrud(
        categoryRepoTest = categoryRepoTest,
        categoryRepoProd = categoryRepoProd
    )

    val service = CategoryService(categoryCrud)
    val itemService = ItemService(itemCrud)

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

    install(StatusPages){
        exception<NumberFormatException> {cause ->
            call.respond(HttpStatusCode.fromValue(service.errorHandler(cause)))
        }
        exception<StringIndexOutOfBoundsException> { cause ->
            call.respond(HttpStatusCode.fromValue(service.errorHandler(cause))) }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        route("/catalogue") {
            post("/get") {
                try {
                    val query = call.receive<CategoryGetQuery>()
                    call.respond(service.get(query))
                }
                catch (e: Exception){
                    call.respond(HttpStatusCode.fromValue(service.errorHandler(e)))
                }
            }
            post("/create") {
                try {
                    val query = call.receive<CategoryCreateQuery>()
                    call.respond(service.create(query))
                }
                catch (e: Exception){
                    call.respond(HttpStatusCode.fromValue(service.errorHandler(e)))
                }
            }
            post("/delete") {
                try {
                    val query = call.receive<CategoryDeleteQuery>()
                    call.respond(service.delete(query))
                }
                catch (e: Exception){
                    call.respond(HttpStatusCode.fromValue(service.errorHandler(e)))
                }
            }
            post("/rename") {
                try {
                    val query = call.receive<CategoryRenameQuery>()
                    call.respond(service.rename(query))
                }
                catch (e: Exception){
                    call.respond(HttpStatusCode.fromValue(service.errorHandler(e)))
                }
            }
            post("/map") {
                try {
                    val query = call.receive<CategoryGetMapQuery>()
                    call.respond(service.getMap(query))
                }
                catch (e: Exception){
                    call.respond(HttpStatusCode.fromValue(service.errorHandler(e)))
                }
            }
            post("/addItem") {
                try {
                    val query = call.receive<ItemCreateQuery>()
                    call.respond(itemService.addItem(query))
                }
                catch (e: Exception){
                    call.respond(HttpStatusCode.fromValue(service.errorHandler(e)))
                }
            }
            post("/delItem") {
                try {
                    val query = call.receive<ItemDeleteQuery>()
                    call.respond(itemService.delItem(query))
                }
                catch (e: Exception){
                    call.respond(HttpStatusCode.fromValue(service.errorHandler(e)))
                }
            }

        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }
    }
}


