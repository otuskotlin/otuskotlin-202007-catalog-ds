package ru.otus.otuskotlin.catalogue.transport.rest.service


import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.logics.items.ItemCrud
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses.CategoryGetResponse
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemResponse
import ru.otus.otuskotlin.catalogue.transport.rest.setQuery

class ItemService(private val crud: ItemCrud): MainService() {


    suspend fun addItem(query: ItemCreateQuery) = ItemContext().queryHandle<ItemResponse> {
        runBlocking {
            crud.create((this@queryHandle as ItemContext).setQuery(query))
        }
    }

    suspend fun delItem(query: ItemDeleteQuery) = ItemContext().queryHandle<ItemResponse> {
        runBlocking {
            crud.delete((this@queryHandle as ItemContext).setQuery(query))
        }
    }

}