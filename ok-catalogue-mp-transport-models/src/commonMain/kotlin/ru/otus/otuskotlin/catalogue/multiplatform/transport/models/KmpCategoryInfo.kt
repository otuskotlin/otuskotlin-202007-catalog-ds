package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryInfo(
        var id:String? = null,
        //var type:String? = null,
        var label:String? = null
        //var creationDate:String? = null,
        //var modifyDate:String? = null
) {
}