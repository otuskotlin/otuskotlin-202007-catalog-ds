package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class KmpCategoryResponse(
        @Transient open var parentList:MutableList<KmpCategoryInfo>? = null,
        @Transient open var item:KmpCategoryInfo? = null,
        @Transient open var status:KmpCategoryError? = null
) {
}