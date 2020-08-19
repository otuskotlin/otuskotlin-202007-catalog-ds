package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryCreateQuery(
        var parentId:String? = null,
        var type:String? = null,
        var label:String? = null,
        var debug:Debug? = null
) {
    @Serializable
    class Debug {

    }
}