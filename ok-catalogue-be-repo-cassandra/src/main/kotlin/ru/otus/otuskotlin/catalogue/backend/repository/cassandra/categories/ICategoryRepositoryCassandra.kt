package ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories

import kotlinx.coroutines.CoroutineScope
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.IItemRepositoryCassandra
import java.io.Closeable

interface ICategoryRepositoryCassandra: ICategoryRepository, CoroutineScope, Closeable {

    fun init(): ICategoryRepositoryCassandra

    fun addItemRepository(repository: IItemRepositoryCassandra): ICategoryRepositoryCassandra
}