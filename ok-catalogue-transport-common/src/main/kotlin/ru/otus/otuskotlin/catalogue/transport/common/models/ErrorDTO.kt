package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
        var code:String? = null,
        val group: String? = null,
        val field: String? = null,
        var level: Level? = null,
        var message:String? = null
) {
        @Serializable
        enum class Level{
            WARNING,
            ERROR,
            SUCCESS
        }
}