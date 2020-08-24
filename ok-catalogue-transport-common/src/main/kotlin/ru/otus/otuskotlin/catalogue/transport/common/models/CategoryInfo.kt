package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryInfo(
        var id:String? = null,
        //var type:String? = null,
        var label:String? = null
        //var creationDate:String? = null,
        //var modifyDate:String? = null
) {
}