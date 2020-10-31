package ru.otus.otuskotlin.catalogue.backend.common.repositories

import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel

interface IItemRepository {
    suspend fun add(item: ItemModel): ItemModel

    suspend fun delete(id: String): ItemModel

    suspend fun get(id: String): ItemModel

    companion object{
        val NONE = object : IItemRepository{
            override suspend fun add(item: ItemModel): ItemModel {
                TODO("Not yet implemented")
            }

            override suspend fun delete(id: String): ItemModel {
                TODO("Not yet implemented")
            }

            override suspend fun get(id: String): ItemModel {
                TODO("Not yet implemented")
            }

        }
    }
}