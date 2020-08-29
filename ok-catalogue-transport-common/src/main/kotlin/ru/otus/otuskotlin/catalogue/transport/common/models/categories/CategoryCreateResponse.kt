package ru.otus.otuskotlin.catalogue.transport.common.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCreateResponse(
        override var parentList: MutableList<CategoryInfo>? = null,
        override var type:String? = null,
        override var label: String? = null,
        override var status: CategoryError? = null
): CategoryResponse(
        parentList = parentList,
        type = type,
        label = label,
        status = status
){
}