package ru.otus.otuskotlin.catalogue.backend.common.dsl

import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel

@CategoryDslMarker
class CategoryParentsDsl(
        var parentId: String = ""
) {
    private val parents: MutableList<CategoryModel> = mutableListOf()

    fun add(model: CategoryModel){
        parents += model
    }

    operator fun CategoryModel.unaryPlus() = add(this)

    fun get() = parents

    companion object{
        val EMPTY = CategoryParentsDsl()
    }
}
