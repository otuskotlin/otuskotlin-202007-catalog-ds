package ru.otus.otuskotlin.catalogue.multiplatform.transport.models

import kotlinx.serialization.Serializable

@Serializable
data class KmpCategoryError(
        var code:String? = null,
        var level:Level? = null,
        var message:String? = null
) {
        @Serializable
        enum class Level{
            SUCCESS,
            WARNING,
            ERROR
        }
}