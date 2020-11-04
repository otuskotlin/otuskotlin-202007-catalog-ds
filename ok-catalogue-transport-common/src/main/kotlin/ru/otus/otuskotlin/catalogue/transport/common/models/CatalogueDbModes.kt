package ru.otus.otuskotlin.catalogue.transport.common.models

import kotlinx.serialization.Serializable

@Serializable
enum class CatalogueDbModes {
    PROD,
    TEST
}