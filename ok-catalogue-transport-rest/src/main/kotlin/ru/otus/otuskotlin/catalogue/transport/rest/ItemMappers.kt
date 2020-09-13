package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.items.*
import java.lang.Exception

/**
 *  File contained mappers for operations with items
 */

fun CategoryContext.setQuery(delItem: ItemDeleteQuery) = this.apply {
    requestCategoryId = delItem.categoryId?:""
    requestItemId = delItem.itemId?:""
}

/**
 *  TODO: To make common for all ItemInfo children
 */
fun CategoryContext.setQuery(addItem: ItemCreateQuery) = this.apply {
    requestCategoryId = addItem.categoryId?:""
    requestItem = addItem.model()
}


fun CategoryContext.resultItem() = ItemResponse(
    data = responseItem.toDTO(),
    status = status.toDTO()
)

///**
// *  TODO: To make common for all ItemInfo children like toDTO fun
// */
//internal fun NoteCreateQuery.model() = NoteModel(
//    id = id?:"",
//    header = header?:"",
//    description = description?:"",
//    preview = preview?:""
//)

fun ItemCreateQuery.model(): ItemModel{
    when(this){
        is NoteCreateQuery -> return NoteModel(
            id = id?:"",
            header = header?:"",
            description = description?:"",
            preview = preview?:""
        )
        else -> throw Exception("Undefined type: ${this::class}.")
    }
}

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


