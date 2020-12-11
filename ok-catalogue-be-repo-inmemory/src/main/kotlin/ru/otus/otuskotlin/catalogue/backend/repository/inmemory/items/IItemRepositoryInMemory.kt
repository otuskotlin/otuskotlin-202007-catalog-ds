package ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items

import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.IItemRepository

interface IItemRepositoryInMemory: IItemRepository {

    suspend fun index(categoryId: String): Collection<ItemModel>?
}