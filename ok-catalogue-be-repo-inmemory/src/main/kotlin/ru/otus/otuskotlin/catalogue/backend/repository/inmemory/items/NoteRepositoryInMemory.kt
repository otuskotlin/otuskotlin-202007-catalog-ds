package ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.CategoryRepoWrongIdException
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.ItemRepoNotFoundException
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.ItemRepoWromgIdException
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.IItemRepository
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class NoteRepositoryInMemory @OptIn(ExperimentalTime::class) constructor(
        ttl: Duration,
        initObjects: Collection<NoteModel> = emptyList()
): IItemRepository {
    @OptIn(ExperimentalTime::class)
    private var cache: Cache<String, NoteInMemoryDTO> = object : Cache2kBuilder<String, NoteInMemoryDTO>() {}
            .expireAfterWrite(ttl.toLongMilliseconds(), TimeUnit.MILLISECONDS) // expire/refresh after 5 minutes
            .suppressExceptions(false)
            .build()
            .also { cache ->
                initObjects.forEach {
                    cache.put(it.id, NoteInMemoryDTO.of(it))
                }
            }

    override suspend fun add(item: ItemModel): ItemModel {
        if (item.id.isBlank()) throw ItemRepoWromgIdException(item.id)
        if (item is NoteModel){
            val dto = NoteInMemoryDTO.of(item)
            cache.put(item.id, dto)
            return cache.get(item.id).toModel()
        }
        throw ClassCastException("Expected: NoteModel, Found: ${item::class}")
    }

    override suspend fun delete(id: String): ItemModel {
        val model = get(id)
        cache.peekAndRemove(id)
        return model
    }

    override suspend fun get(id: String): ItemModel {
        if (id.isBlank()) throw ItemRepoWromgIdException(id)
        return cache.get(id)?.toModel()?: throw ItemRepoNotFoundException(id)
    }

    override suspend fun index(categoryId: String): Collection<ItemModel>? {
        if (categoryId.isBlank()) throw CategoryRepoWrongIdException(categoryId)
        return cache.entries()?.filter {
            it.value.categoryId == categoryId
        }?.map { it.value.toModel() }
    }
}