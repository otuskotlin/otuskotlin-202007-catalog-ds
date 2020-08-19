package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryCreateResponse(
        override var parentList: MutableList<KmpCategoryInfo>? = null,
        override var item: KmpCategoryInfo? = null,
        override var status: KmpCategoryError? = null
): KmpCategoryResponse(
        parentList = parentList,
        item = item,
        status = status
){
}