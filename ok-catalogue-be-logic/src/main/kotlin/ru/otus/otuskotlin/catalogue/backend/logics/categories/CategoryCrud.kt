package ru.otus.otuskotlin.catalogue.backend.logics.categories

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext

class CategoryCrud {
    private val getChain = CategoryGetChain()
    private val createChain = CategoryCreateChain()
    private val renameChain = CategoryRenameChain()
    private val deleteChain = CategoryDeleteChain()
    private val getMapChain = CategoryGetMapChain()

    suspend fun get(context: CategoryContext) = getChain.exec(context)
    suspend fun getMap(context: CategoryContext) = getMapChain.exec(context)
    suspend fun create(context: CategoryContext) = createChain.exec(context)
    suspend fun delete(context: CategoryContext) = deleteChain.exec(context)
    suspend fun rename(context: CategoryContext) = renameChain.exec(context)

}