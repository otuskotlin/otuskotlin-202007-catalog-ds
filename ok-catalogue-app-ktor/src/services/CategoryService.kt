package ru.otus.otuskotlin.services

import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.*
import ru.otus.otuskotlin.catalogue.transport.rest.resultCategory
import ru.otus.otuskotlin.catalogue.transport.rest.resultMap
import ru.otus.otuskotlin.catalogue.transport.rest.setQuery
import java.lang.Exception
import java.time.LocalDate

/**
 * @TODO: Replace class into rest module
 * @TODO: Refactor tryCategoryQuery with reified
 */
class CategoryService(): MainService() {


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
            responseCategory = requestCategory.copy(id = "asdf")
        }
        resultCategory()
    }

    suspend fun delete(query: CategoryDeleteQuery) = CategoryContext().run {
        tryCategoryQuery("Category delete chain error"){
            setQuery(query)
            responseCategory = categoryModel.apply {
                children.removeIf { it.id == query.categoryId?: throw Exception("No id.") }
                }.copy()
        }
        resultCategory()
    }

    suspend fun rename(query: CategoryRenameQuery) = CategoryContext().run {
        tryCategoryQuery("Category rename chain error"){
            setQuery(query)
            responseCategory = categoryModel.copy(label = query.modLabel?: throw Exception("No label."))
        }
        resultCategory()
    }

    suspend fun map(query: CategoryGetMapQuery) = CategoryContext().run {
        tryCategoryQuery("Category rename chain error"){
            setQuery(query)
            responseCategory = categoryModel.copy(id = query.id?: throw Exception("No id."))
        }
        resultMap()
    }


}