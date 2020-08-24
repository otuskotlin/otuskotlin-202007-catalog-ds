package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemInfo

@Serializable
data class CategoryGetResponse(
        override var parentList:MutableList<CategoryInfo>? = null,
        var childList: MutableList<CategoryInfo>? = null,
        var itemList: MutableList<ItemInfo>? = null,
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