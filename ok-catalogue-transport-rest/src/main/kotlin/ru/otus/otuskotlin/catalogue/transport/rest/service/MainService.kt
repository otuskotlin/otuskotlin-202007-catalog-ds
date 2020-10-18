package ru.otus.otuskotlin.catalogue.transport.rest.service

import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.catalogue.backend.common.contexts.BaseContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.errors.InternalServerError
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.ResponseModel
import ru.otus.otuskotlin.catalogue.transport.rest.getResult
import java.lang.Exception
import java.lang.NumberFormatException
import java.time.LocalDate

open class MainService() {
    protected val log = LoggerFactory.getLogger(this::class.java)

    protected val itemModel = NoteModel(
            id = "12",
            header = "My note",
            description = "Some note",
            preview = "qwerty"
    )

    protected val categoryModel = CategoryModel(
            id = "12345",
            label = "Notes",
            type = "notes",
            children = mutableSetOf(CategoryModel(id = "12346", label = "Subdir")),
            items = mutableSetOf(itemModel),
            creationDate = LocalDate.of(2010, 6, 13)
    )

    suspend fun errorHandler(error: Throwable):Int {
        log.error("Input query error.", error)
        return when(error::class){
            StringIndexOutOfBoundsException::class -> 502
            else -> 400
        }
    }

    protected suspend inline fun <reified T: ResponseModel> BaseContext.queryHandle(
            crossinline action: BaseContext.()-> Unit) = run{
                try{
                    action()
                }
                catch (e: Exception){
                    log.error("setQuery error.", e)
                    errors.add(InternalServerError.instance)
                }
                getResult<T>()
            }
    }