package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryError

@Serializable
data class ItemResponse(
    var data: ItemInfo? = null,
    var status: CategoryError? = null
) {
}