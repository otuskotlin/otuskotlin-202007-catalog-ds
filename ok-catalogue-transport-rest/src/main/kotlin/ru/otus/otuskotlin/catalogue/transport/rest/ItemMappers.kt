package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemInfo
import ru.otus.otuskotlin.catalogue.transport.common.models.items.NoteCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.NoteInfo
import java.lang.Exception

/**
 *  File contained mappers for operations with items
 */


internal fun CategoryContext.setQuery(delItem: ItemDeleteQuery) = this.apply {
    requestCategoryId = delItem.categoryId?:""
    requestItemId = delItem.itemId?:""
}

/**
 *  TODO: To make common for all ItemInfo children
 */
internal fun CategoryContext.setQuery(addItem: NoteCreateQuery) = this.apply {
    requestCategoryId = addItem.categoryId?:""
    requestItem = addItem.model()
}

/**
 *  TODO: To make common for all ItemInfo children like toDTO fun
 */
internal fun NoteCreateQuery.model() = NoteModel(
    id = id?:"",
    header = header?:"",
    description = description?:"",
    preview = preview?:""
)

fun ItemModel.toDTO(): ItemInfo{
    when(this){
        is NoteModel -> return NoteInfo(
                id = id.toDTOString(),
                header = header.toDTOString(),
                description = description.toDTOString(),
                preview = preview.toDTOString()
        )
        else -> throw Exception("Undefined type: ${this::class}.")
    }
}


