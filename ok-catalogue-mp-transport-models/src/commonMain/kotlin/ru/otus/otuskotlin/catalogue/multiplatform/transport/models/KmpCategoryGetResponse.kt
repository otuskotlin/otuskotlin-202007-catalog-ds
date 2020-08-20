package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryGetResponse(
        override var parentList:MutableList<KmpCategoryInfo>? = null,
        var childList: MutableList<KmpCategoryInfo>? = null,
        override var type:String? = null,
        override var label: String? = null,
        override var status:KmpCategoryError? = null
): KmpCategoryResponse(
        parentList = parentList,
        type = type,
        label = label,
        status = status
) {
}