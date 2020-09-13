package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Serializable

@Serializable
data class NoteInfo(
        override var categoryId: String? = null,
        override var id: String? = null,
        override var header: String? = null,
        override var description: String? = null,
        var preview:String? = null
):ItemInfo(
        categoryId = categoryId,
        id = id,
        header = header,
        description = description
)