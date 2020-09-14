package ru.otus.otuskotlin.services

import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryGetResponse
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemInfo
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemResponse
import ru.otus.otuskotlin.catalogue.transport.rest.getResult
import ru.otus.otuskotlin.catalogue.transport.rest.resultCategory
import ru.otus.otuskotlin.catalogue.transport.rest.resultItem
import ru.otus.otuskotlin.catalogue.transport.rest.setQuery
import java.time.LocalDate

class ItemService(): MainService() {


    suspend fun addItem(query: ItemCreateQuery) = CategoryContext().run {
        tryCategoryQuery(""){
            setQuery(query)
            responseItem = requestItem
        }
        getResult<ItemResponse>()
        //resultItem()
    }

    suspend fun delItem(query: ItemDeleteQuery) = CategoryContext().run {
        tryCategoryQuery(""){
            setQuery(query)
            responseCategory = categoryModel.apply {
                items.removeIf { it.id == query.itemId }
            }.copy()
        }
        getResult<CategoryGetResponse>()
        //resultCategory()
    }
}