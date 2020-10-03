package ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.catalogue.transport.common.models.ErrorDTO
import ru.otus.otuskotlin.catalogue.transport.common.models.ResponseModel

@Serializable
data class CategoryGetMapResponse(
        var data: CategoryMapDTO? = null,
        override var status: ErrorDTO? = null
): ResponseModel(
        status = status
)