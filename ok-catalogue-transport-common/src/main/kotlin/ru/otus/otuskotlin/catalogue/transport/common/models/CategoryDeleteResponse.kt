package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDeleteResponse(
    var status:CategoryError? = null
)
{}