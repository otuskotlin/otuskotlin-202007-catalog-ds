package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
//
//@Serializable
//abstract class ItemCreateQuery (
//    @Transient open var categoryId: String? = null
//)

@Serializable
sealed class ItemCreateQuery(
   @Transient open var categoryId: String? = null
)

@Serializable
@SerialName("note")
data class NoteCreateQuery(
    var id: String? = null,
    override var categoryId: String? = null,
    var header: String? = null,
    var description: String? = null,
    var preview: String? = null,
    var debug: Debug? = null
): ItemCreateQuery(
    categoryId = categoryId
)
{
    @Serializable
    class Debug

    }
