package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryError(
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