package ru.otus.otuskotlin.catalogue.backend.common.dsl

import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel

@CategoryDslMarker
class CategoryItemsDsl {
    private val items: MutableSet<ItemModel> = mutableSetOf()

    fun add(item: ItemModel){
        items += item
    }

    operator fun ItemModel.unaryPlus() = add(this)

    fun get() = items

    companion object{
        val EMPTY = CategoryItemsDsl()
    }
}
