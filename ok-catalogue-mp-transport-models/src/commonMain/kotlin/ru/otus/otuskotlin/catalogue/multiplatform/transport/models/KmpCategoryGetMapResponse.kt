package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryGetMapResponse(
    var id:String? = null,
    var label:String? = null,
    var children:MutableList<KmpCategoryGetMapResponse>? = null
) {

}