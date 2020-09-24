package ru.otus.otuskotlin.catalogue.backend.common.dsl

class CategoryNameDsl(
        var label: String = ""
) {

    companion object{
        val EMPTY = CategoryNameDsl()
    }
}
