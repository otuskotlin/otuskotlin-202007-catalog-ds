package ru.otus.otuskotlin

import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.*
import ru.otus.otuskotlin.catalogue.transport.rest.resultCategory
import ru.otus.otuskotlin.catalogue.transport.rest.setQuery
import java.lang.Exception
import java.time.LocalDate
import java.util.*

class CategoryService() {
    private val log = LoggerFactory.getLogger(this::class.java)

    private val itemModel = NoteModel(
            id = "12",
            header = "My note",
            description = "Some note",
            preview = "qwerty"
    )

    private val categoryModel = CategoryModel(
            id = "12345",
            label = "Notes",
            type = "notes",
            children = mutableSetOf(CategoryModel(id = "12346", label = "Subdir")),
            items = mutableSetOf(itemModel),
            creationDate = LocalDate.of(2010, 6, 13)
    )

    suspend fun get(query: CategoryGetQuery) = CategoryContext().run {
        tryCategoryQuery("Category get chain error") {
            setQuery(query)
            responseCategory = categoryModel.copy(id = query.categoryId?: throw Exception("No id."))
        }
        resultCategory()

    }

    suspend fun create(query: CategoryCreateQuery) = CategoryContext().run {
        tryCategoryQuery("Category create chain error"){
            setQuery(query)
            categoryModel.copy(id = UUID.randomUUID().toString())
        }
        resultCategory()
    }


    private suspend inline fun CategoryContext.tryCategoryQuery(errorMessage: String, action: () -> Unit){
        try {
            action()
            status = CategoryContextStatus.SUCCESS
        }
        catch (e: Exception){
            log.error(errorMessage, e)
            status = CategoryContextStatus.ERROR
        }


    }
}