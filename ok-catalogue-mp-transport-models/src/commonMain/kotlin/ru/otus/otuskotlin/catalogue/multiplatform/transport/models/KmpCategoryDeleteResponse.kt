package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryDeleteResponse(
    var status:KmpCategoryError? = null
)
{}