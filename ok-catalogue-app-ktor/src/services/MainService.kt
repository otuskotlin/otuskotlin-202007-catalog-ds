package ru.otus.otuskotlin.services

import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import java.lang.Exception
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

    protected suspend inline fun CategoryContext.tryCategoryQuery(errorMessage: String, action: () -> Unit){
        status = try {
            action()
            CategoryContextStatus.SUCCESS
        } catch (e: Exception){
            log.error(errorMessage, e)
            CategoryContextStatus.ERROR
        }


    }
}