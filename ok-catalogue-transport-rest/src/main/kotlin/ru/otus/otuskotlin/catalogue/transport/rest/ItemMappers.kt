package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.NoteCreateQuery


internal fun CategoryContext.setQuery(delItem: ItemDeleteQuery) = this.apply {
    requestCategoryId = delItem.categoryId?:""
    requestItemId = delItem.itemId?:""
}

internal fun CategoryContext.setQuery(addItem: NoteCreateQuery) = this.apply {
    requestCategoryId = addItem.categoryId?:""
    requestItem = addItem.model()
}

internal fun NoteCreateQuery.model() = NoteModel(
    id = id?:"",
    header = header?:"",
    description = description?:"",
    preview = preview?:""
)