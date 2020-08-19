package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryGetResponse(
        override var parentList:MutableList<KmpCategoryInfo>? = null,
        var childList: MutableList<KmpCategoryInfo>? = null,
        override var item:KmpCategoryInfo? = null,
        override var status:KmpCategoryError? = null
): KmpCategoryResponse(
        parentList = parentList,
        item = item,
        status = status
) {
}