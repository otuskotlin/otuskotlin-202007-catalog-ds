package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Serializable

@Serializable
abstract class ItemInfo(
    @Transient open var id: String? = null,
    @Transient open var header: String? = null,
    @Transient open var description: String? = null
) {
}