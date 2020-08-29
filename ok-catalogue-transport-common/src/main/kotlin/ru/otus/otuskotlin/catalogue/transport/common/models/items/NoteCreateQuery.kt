package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Serializable

@Serializable
data class NoteCreateQuery(
    override var id: String? = null,
    override var categoryId: String? = null,
    override var header: String? = null,
    override var description: String? = null,
    var preview: String? = null
):ItemInfo(
    id = id,
    categoryId = categoryId,
    header = header,
    description = description
) {
}