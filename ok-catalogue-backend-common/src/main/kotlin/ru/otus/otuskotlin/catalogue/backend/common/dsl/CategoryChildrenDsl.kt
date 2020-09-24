package ru.otus.otuskotlin.catalogue.backend.common.dsl

import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel

@CategoryDslMarker
class CategoryChildrenDsl {
    private val children: MutableSet<CategoryModel> = mutableSetOf()

    fun add(model: CategoryModel){
        children += model
    }

    operator fun CategoryModel.unaryPlus() = add(this)

    fun get() = children

    companion object{
        val EMPTY = CategoryChildrenDsl()
    }
}
