package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryGetMapQuery(
    var id:String? = null,
    var debug:Debug? = null
) {
    @Serializable
    class Debug
}