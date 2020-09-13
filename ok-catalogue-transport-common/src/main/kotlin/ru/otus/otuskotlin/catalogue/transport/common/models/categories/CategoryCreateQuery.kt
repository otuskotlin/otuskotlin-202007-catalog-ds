package ru.otus.otuskotlin.catalogue.transport.common.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCreateQuery(
        var parentId:String? = null,
        var type:String? = null,
        var label:String? = null,
        var debug: Debug? = null
) {
    @Serializable
    class Debug {

    }
}