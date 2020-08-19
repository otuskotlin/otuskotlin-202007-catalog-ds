package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryGetQuery (
        var categoryId:String? = null,
        var debug:Debug? = null
) {
    @Serializable
    class Debug
}