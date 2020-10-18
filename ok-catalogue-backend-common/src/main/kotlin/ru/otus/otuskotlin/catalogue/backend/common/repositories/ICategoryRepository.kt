package ru.otus.otuskotlin.catalogue.backend.common.repositories

import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel

interface ICategoryRepository {

    suspend fun get(id: String): CategoryModel
    suspend fun getMap(id: String): Collection<CategoryModel>
    suspend fun create(category: CategoryModel): CategoryModel
    suspend fun rename(id: String, label: String): CategoryModel
    suspend fun delete(id: String): CategoryModel

    companion object{
        val NONE = object : ICategoryRepository{
            override suspend fun get(id: String): CategoryModel {
                TODO("Not yet implemented")
            }

            override suspend fun getMap(id: String): Collection<CategoryModel> {
                TODO("Not yet implemented")
            }

            override suspend fun create(category: CategoryModel): CategoryModel {
                TODO("Not yet implemented")
            }

            override suspend fun rename(id: String, label: String): CategoryModel {
                TODO("Not yet implemented")
            }

            override suspend fun delete(id: String): CategoryModel {
                TODO("Not yet implemented")
            }

        }
    }
}