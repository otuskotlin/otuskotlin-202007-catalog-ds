package ru.otus.otuskotlin.catalogue.transport.common.models.categories.queries

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRenameQuery(
    var categoryId: String? = null,
    var modLabel: String? = null,
    var debug: Debug? = null
){
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