package ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.catalogue.transport.common.models.ErrorDTO
import ru.otus.otuskotlin.catalogue.transport.common.models.ResponseModel
import ru.otus.otuskotlin.catalogue.transport.common.models.StatusDTO

@Serializable
data class CategoryGetMapResponse(
        var data: CategoryMapDTO? = null,
        override var status: StatusDTO? = null,
        override var errors: List<ErrorDTO>? = null
): ResponseModel(
        status = status,
        errors = errors
)