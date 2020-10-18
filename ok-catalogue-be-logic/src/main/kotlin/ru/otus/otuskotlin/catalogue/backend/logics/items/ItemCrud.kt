package ru.otus.otuskotlin.catalogue.backend.logics.items

import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext

class ItemCrud {
    private val createChain = ItemCreateChain()
    private var deleteChain = ItemDeleteChain()

    suspend fun create(context: ItemContext) = createChain.exec(context)
    suspend fun delete(context: ItemContext) = deleteChain.exec(context)

    companion object{
        val DEFAULT = ItemCrud()
    }
}