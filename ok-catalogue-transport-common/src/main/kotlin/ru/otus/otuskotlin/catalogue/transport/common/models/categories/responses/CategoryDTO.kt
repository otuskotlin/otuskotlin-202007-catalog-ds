package ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemInfo

@Serializable
data class CategoryDTO(
        var id: String? = null,
        var type:String? = null,
        var label: String? = null,
        var parentList:MutableList<CategoryInfo>? = null,
        var childList: MutableList<CategoryInfo>? = null,
        //@Polymorphic var itemList: MutableList<ItemInfo>? = null,
        var itemList: MutableList<ItemInfo>? = null,
        var creationDate: String? = null,
        var modifyDate: String? = null
)