package ru.otus.otuskotlin.catalogue.transport.rest.service

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryGetResponse
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemResponse
import ru.otus.otuskotlin.catalogue.transport.rest.setQuery

class ItemService(): MainService() {


    suspend fun addItem(query: ItemCreateQuery) = CategoryContext().queryHandle<ItemResponse> {
        setQuery(query)
        responseItem = requestItem
    }

    suspend fun delItem(query: ItemDeleteQuery) = CategoryContext().queryHandle<CategoryGetResponse> {
        setQuery(query)
        responseCategory = categoryModel.apply {
                items.removeIf { it.id == query.itemId }
            }.copy()
    }

}