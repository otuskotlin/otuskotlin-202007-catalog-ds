package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
@SerialName("NoteInfo")
data class NoteInfo(
        //override var  type: KClass<ItemInfo>? = null,
        override var categoryId: String? = null,
        override var id: String? = null,
        override var header: String? = null,
        override var description: String? = null,
        var preview:String? = null
):ItemInfo(
        //type = type,
        categoryId = categoryId,
        id = id,
        header = header,
        description = description
)