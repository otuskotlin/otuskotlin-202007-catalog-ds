package ru.otus.otuskotlin.catalogue.transport.common.models.categories

import kotlinx.serialization.Serializable

/**
 *  Tree of catalogue structure
 */
@Serializable
data class CategoryMapDTO(
        var id:String? = null,
        var label:String? = null,
        var children:MutableList<CategoryMapDTO>? = null
)