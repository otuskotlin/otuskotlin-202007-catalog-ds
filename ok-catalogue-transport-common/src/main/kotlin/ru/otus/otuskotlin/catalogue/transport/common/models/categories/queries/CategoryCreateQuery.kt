package ru.otus.otuskotlin.catalogue.transport.common.models.categories.queries

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCreateQuery(
        var parentId:String? = null,
        var type:String? = null,
        var label:String? = null,
        var debug: Debug? = null
) {
    @Serializable
    data class Debug (
        val stub: StubCases? = null
    )

    @Serializable
    enum class StubCases{
        NONE,
        SUCCESS
    }
}