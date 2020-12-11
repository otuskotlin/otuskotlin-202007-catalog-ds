package ru.otus.otuskotlin.catalogue.backend.logics.items

import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository

class ItemCrud(
    categoryRepoTest: ICategoryRepository = ICategoryRepository.NONE,
    categoryRepoProd: ICategoryRepository = ICategoryRepository.NONE
) {
    private val createChain = ItemCreateChain(categoryRepoTest, categoryRepoProd)
    private var deleteChain = ItemDeleteChain(categoryRepoTest, categoryRepoProd)

    suspend fun create(context: ItemContext) = createChain.exec(context)
    suspend fun delete(context: ItemContext) = deleteChain.exec(context)

    companion object{
        val DEFAULT = ItemCrud()
    }
}