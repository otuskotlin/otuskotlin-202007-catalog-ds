package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryGetResponse(
        override var parentList:MutableList<CategoryInfo>? = null,
        var childList: MutableList<CategoryInfo>? = null,
        override var type:String? = null,
        override var label: String? = null,
        override var status:CategoryError? = null
): CategoryResponse(
        parentList = parentList,
        type = type,
        label = label,
        status = status
) {
}