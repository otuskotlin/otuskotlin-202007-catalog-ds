package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class ItemInfo(
        @Transient open var id: String? = null,
        @Transient open var categoryId: String? = null,
        @Transient open var header: String? = null,
        @Transient open var description: String? = null
) {
}