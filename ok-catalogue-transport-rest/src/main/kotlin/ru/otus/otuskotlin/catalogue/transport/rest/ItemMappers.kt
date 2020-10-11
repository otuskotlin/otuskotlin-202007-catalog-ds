package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemDeleteStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.items.*
import java.lang.Exception

/**
 *  File contained mappers for operations with items
 */

fun ItemContext.setQuery(delItem: ItemDeleteQuery) = this.apply {
    requestCategoryId = delItem.categoryId?:""
    requestItemId = delItem.itemId?:""
    stubIDeleteCase = when(delItem.debug?.stub){
        ItemDeleteQuery.StubCases.SUCCESS -> ItemDeleteStubCases.SUCCESS
        else -> ItemDeleteStubCases.NONE
    }
}


fun ItemContext.setQuery(addItem: ItemCreateQuery) = this.apply {
    requestCategoryId = addItem.categoryId?:""
    requestItem = addItem.model()
    stubICreateCase = when(addItem.debug?.stub){
        ItemCreateQuery.StubCases.SUCCESS -> ItemCreateStubCases.SUCCESS
        else -> ItemCreateStubCases.NONE
    }
}


fun ItemContext.resultItem() = ItemResponse(
    data = responseItem.toDTO(),
    status = this.toStatusDTO(),
    errors = errors.map { it.toErrorDTO() }
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


