package ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.catalogue.transport.common.models.ErrorDTO
import ru.otus.otuskotlin.catalogue.transport.common.models.ResponseModel

/**
 *  Model for all responses by categories queries
 */
@Serializable
data class CategoryGetResponse(
        var data: CategoryDTO? = null,
        override var status: ErrorDTO? = null
): ResponseModel(
        status = status
)