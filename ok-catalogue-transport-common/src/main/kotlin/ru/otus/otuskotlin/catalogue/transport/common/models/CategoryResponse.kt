package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class CategoryResponse(
        @Transient open var parentList:MutableList<CategoryInfo>? = null,
        @Transient open var type:String? = null,
        @Transient open var label:String? = null,
        @Transient open var status:CategoryError? = null
) {
}