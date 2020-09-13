package ru.otus.otuskotlin.catalogue.transport.common.models.categories

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemInfo

/**
 *  Model for all responses by categories queries
 */
@Serializable
data class CategoryGetResponse(
        var data: CategoryDTO? = null,
        var status: CategoryError? = null
) {
}