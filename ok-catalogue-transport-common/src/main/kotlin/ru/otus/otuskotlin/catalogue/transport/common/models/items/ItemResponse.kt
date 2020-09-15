package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.catalogue.transport.common.models.ErrorDTO
import ru.otus.otuskotlin.catalogue.transport.common.models.ResponseModel

@Serializable
data class ItemResponse(
    var data: ItemInfo? = null,
    override var status: ErrorDTO? = null
): ResponseModel(
        status = status
)