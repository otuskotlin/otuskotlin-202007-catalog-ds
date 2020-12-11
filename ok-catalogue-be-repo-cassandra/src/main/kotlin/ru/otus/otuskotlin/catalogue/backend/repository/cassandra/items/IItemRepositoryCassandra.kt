package ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items

import kotlinx.coroutines.CoroutineScope
import ru.otus.otuskotlin.catalogue.backend.common.repositories.IItemRepository
import java.io.Closeable

interface IItemRepositoryCassandra: IItemRepository, CoroutineScope, Closeable {

    fun init(): IItemRepositoryCassandra
}