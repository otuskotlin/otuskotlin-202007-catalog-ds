package ru.otus.otuskotlin.catalogue.backend.common.models.items


abstract class ItemModel(
    open var id: String = "",
    open var categoryId: String = "",
    open var header: String = "",
    open var description: String = ""
) {
    companion object{
        val NONE = object : ItemModel() {}
    }
}