package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryDeleteQuery(
        var categoryId:String? = null,
        var debug:Debug? = null
) {
    @Serializable
    class Debug
}