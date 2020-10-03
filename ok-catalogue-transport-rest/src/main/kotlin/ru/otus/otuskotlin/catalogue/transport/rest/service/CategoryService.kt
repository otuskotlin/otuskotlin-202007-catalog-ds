package ru.otus.otuskotlin.catalogue.transport.rest.service

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.logics.categories.CategoryCrud
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.queries.*
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses.CategoryGetMapResponse
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses.CategoryGetResponse
import ru.otus.otuskotlin.catalogue.transport.rest.setQuery


class CategoryService(private val crud: CategoryCrud) : MainService() {


    suspend fun get(query: CategoryGetQuery = CategoryGetQuery()) = CategoryContext().queryHandle<CategoryGetResponse> {
         runBlocking {
            crud.get((this@queryHandle as CategoryContext).setQuery(query))
        }
    }



    suspend fun create(query: CategoryCreateQuery) = CategoryContext().queryHandle<CategoryGetResponse> {
        runBlocking {
            crud.create((this@queryHandle as CategoryContext).setQuery(query))
        }
    }

    suspend fun delete(query: CategoryDeleteQuery) = CategoryContext().queryHandle<CategoryGetResponse> {
        runBlocking {
            crud.delete((this@queryHandle as CategoryContext).setQuery(query))
        }
    }


    suspend fun rename(query: CategoryRenameQuery) = CategoryContext().queryHandle<CategoryGetResponse> {
        runBlocking {
            crud.rename((this@queryHandle as CategoryContext).setQuery(query))
        }
    }

    suspend fun getMap(query: CategoryGetMapQuery) = CategoryContext().queryHandle<CategoryGetMapResponse> {
        runBlocking {
            crud.getMap((this@queryHandle as CategoryContext).setQuery(query))
        }
    }

}