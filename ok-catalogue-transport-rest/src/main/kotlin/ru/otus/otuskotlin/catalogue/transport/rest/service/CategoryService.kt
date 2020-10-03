package ru.otus.otuskotlin.catalogue.transport.rest.service

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.*
import ru.otus.otuskotlin.catalogue.transport.rest.setQuery
import java.lang.Exception

/**
 * @TODO: Replace class into rest module
 * @TODO: Refactor tryCategoryQuery with reified
 */
class CategoryService(): MainService() {


    suspend fun get(query: CategoryGetQuery) = CategoryContext().queryHandle<CategoryGetResponse> {
        setQuery(query)
        responseCategory = categoryModel.copy(id = query.categoryId?:throw Exception("No ID."))
    }



    suspend fun create(query: CategoryCreateQuery) = CategoryContext().queryHandle<CategoryGetResponse> {
        setQuery(query)
        responseCategory = requestCategory.copy(id = "asdf")
    }

    suspend fun delete(query: CategoryDeleteQuery) = CategoryContext().queryHandle<CategoryGetResponse> {
        setQuery(query)
        responseCategory = categoryModel.apply {
                children.removeIf { it.id == query.categoryId?: throw Exception("No id.") }
                }.copy()
    }


    suspend fun rename(query: CategoryRenameQuery) = CategoryContext().queryHandle<CategoryGetResponse> {
        setQuery(query)
        responseCategory = categoryModel.copy(label = query.modLabel?: throw Exception("No label."))
    }

    suspend fun getMap(query: CategoryGetMapQuery) = CategoryContext().queryHandle<CategoryGetMapResponse> {
        setQuery(query)
        responseCategory = categoryModel.copy(id = query.id?: throw Exception("No id."))
    }

}