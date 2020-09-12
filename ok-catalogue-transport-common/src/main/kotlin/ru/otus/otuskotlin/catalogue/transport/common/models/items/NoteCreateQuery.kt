package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Serializable

@Serializable
data class NoteCreateQuery(
        var id: String? = null,
        var categoryId: String? = null,
        var header: String? = null,
        var description: String? = null,
        var preview: String? = null,
        var debug:Debug? = null
)//:ItemInfo(
//    id = id,
//    categoryId = categoryId,
//    header = header,
//    description = description
//)
{
    @Serializable
    class Debug
}