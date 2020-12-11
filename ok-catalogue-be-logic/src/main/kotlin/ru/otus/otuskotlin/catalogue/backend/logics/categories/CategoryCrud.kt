package ru.otus.otuskotlin.catalogue.backend.logics.categories

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository

class CategoryCrud(
    categoryRepoTest: ICategoryRepository = ICategoryRepository.NONE,
    categoryRepoProd: ICategoryRepository = ICategoryRepository.NONE
) {
    private val getChain = CategoryGetChain(categoryRepoTest, categoryRepoProd)
    private val createChain = CategoryCreateChain(categoryRepoTest, categoryRepoProd)
    private val renameChain = CategoryRenameChain(categoryRepoTest, categoryRepoProd)
    private val deleteChain = CategoryDeleteChain(categoryRepoTest, categoryRepoProd)
    private val getMapChain = CategoryGetMapChain(categoryRepoTest, categoryRepoProd)

    suspend fun get(context: CategoryContext) = getChain.exec(context)
    suspend fun getMap(context: CategoryContext) = getMapChain.exec(context)
    suspend fun create(context: CategoryContext) = createChain.exec(context)
    suspend fun delete(context: CategoryContext) = deleteChain.exec(context)
    suspend fun rename(context: CategoryContext) = renameChain.exec(context)

    companion object{
        val DEFAULT = CategoryCrud()
    }
}