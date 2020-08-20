package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class KmpCategoryResponse(
        @Transient open var parentList:MutableList<KmpCategoryInfo>? = null,
        @Transient open var type:String? = null,
        @Transient open var label:String? = null,
        @Transient open var status:KmpCategoryError? = null
) {
}