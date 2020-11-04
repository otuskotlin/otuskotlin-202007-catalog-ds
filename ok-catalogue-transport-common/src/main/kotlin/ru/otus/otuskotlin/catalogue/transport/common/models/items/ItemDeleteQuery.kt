package ru.otus.otuskotlin.catalogue.transport.common.models.items

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.catalogue.transport.common.models.CatalogueDbModes

@Serializable
data class ItemDeleteQuery(
    var itemId: String? = null,
    var categoryId: String? = null,
    var  debug: Debug? = null
) {
    @Serializable
    data class Debug (
            val stub: StubCases? = null,
            val dbMode: CatalogueDbModes? = null
    )

    @Serializable
    enum class StubCases{
        NONE,
        SUCCESS
    }
}