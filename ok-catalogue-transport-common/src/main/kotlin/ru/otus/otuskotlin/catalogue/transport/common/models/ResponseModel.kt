package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class ResponseModel(
        @Transient open var status: ErrorDTO? = null
)