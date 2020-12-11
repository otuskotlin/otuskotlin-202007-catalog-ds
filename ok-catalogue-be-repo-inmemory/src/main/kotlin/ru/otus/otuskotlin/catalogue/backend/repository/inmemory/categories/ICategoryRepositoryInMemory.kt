package ru.otus.otuskotlin.catalogue.backend.repository.inmemory.categories

import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items.IItemRepositoryInMemory

interface ICategoryRepositoryInMemory: ICategoryRepository {

    fun addItemRepository(repository: IItemRepositoryInMemory): ICategoryRepositoryInMemory
}