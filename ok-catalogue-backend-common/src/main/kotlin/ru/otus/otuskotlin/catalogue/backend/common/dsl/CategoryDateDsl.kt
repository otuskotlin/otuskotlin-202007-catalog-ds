package ru.otus.otuskotlin.catalogue.backend.common.dsl

import java.time.LocalDate

@CategoryDslMarker
class CategoryDateDsl(
        var creation: LocalDate = LocalDate.MIN,
        var modify: LocalDate = creation
) {

    companion object{
        val EMPTY = CategoryDateDsl()
    }
}
