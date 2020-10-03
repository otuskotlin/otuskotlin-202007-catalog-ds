package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KClass

//@Serializable
//abstract class ItemInfo(
//        //@Transient open var  type: KClass<ItemInfo>? = null,
//        @Transient open var id: String? = null,
//        @Transient open var categoryId: String? = null,
//        @Transient open var header: String? = null,
//        @Transient open var description: String? = null
//) {
//}

// Решил пока остановиться на sealed, потому что ItemInfo больше нигда не наследуется,
// можно все вместить в один файл
@Serializable
sealed class ItemInfo

@Serializable
@SerialName("note")
data class NoteInfo(
        var categoryId: String? = null,
        var id: String? = null,
        var header: String? = null,
        var description: String? = null,
        var preview:String? = null
): ItemInfo()