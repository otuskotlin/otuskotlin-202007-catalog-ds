package ru.otus.otuskotlin.catalogue.transport.common.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRenameQuery(
    var categoryId: String? = null,
    var modLabel: String? = null
)