package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryGetMapResponse(
    var id:String? = null,
    var label:String? = null,
    var children:MutableList<CategoryGetMapResponse>? = null
) {

}