package ru.otus.otuskotlin.catalogue.backend.repository.inmemory

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class CategoryRepositoryInMemory @OptIn(ExperimentalTime::class) constructor(
    ttl: Duration,
    initObjects: Collection<CategoryModel>
): ICategoryRepository {
    @OptIn(ExperimentalTime::class)
    private var cache: Cache<String, CategoryInMemoryDTO> = object : Cache2kBuilder<String, CategoryInMemoryDTO>() {}
        .expireAfterWrite(ttl.toLongMilliseconds(), TimeUnit.MILLISECONDS) // expire/refresh after 5 minutes
        .suppressExceptions(false)
        .build()
        .also { cache ->
            initObjects.forEach {
                cache.put(it.id, CategoryInMemoryDTO.of(it))
            }
        }

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