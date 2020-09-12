package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KClass

@Serializable
abstract class ItemInfo(
        //@Transient open var  type: KClass<ItemInfo>? = null,
        @Transient open var id: String? = null,
        @Transient open var categoryId: String? = null,
        @Transient open var header: String? = null,
        @Transient open var description: String? = null
) {
}
//@Serializable
//sealed class ItemInfo
//
//@Serializable
//data class NoteInfo(
//        var categoryId: String? = null,
//        var id: String? = null,
//        var header: String? = null,
//        var description: String? = null,
//        var preview:String? = null
//): ItemInfo()