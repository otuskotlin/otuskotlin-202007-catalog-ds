package ru.otus.otuskotlin.catalogue.backend.repository.inmemory

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.CategoryRepoNotFoundException
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.CategoryRepoWrongIdException
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class CategoryRepositoryInMemory @OptIn(ExperimentalTime::class) constructor(
    ttl: Duration,
    initObjects: Collection<CategoryModel> = emptyList()
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
        val  model = getWithChildren(id)
        var parentId:String? = model.parentId
        while (!parentId.isNullOrBlank()){
            val parent = cache.get(parentId)?.toModel()?: throw CategoryRepoNotFoundException(parentId)
            model.parents.add(parent)
            parentId = parent.parentId
        }
                //TODO: Add items
        return model
    }

    override suspend fun getMap(id: String): CategoryModel {
        val model = getWithChildren(id)
        model.children.forEach {
            it.children.add(getMap(it.id))
        }
        return model
    }

    override suspend fun create(category: CategoryModel): CategoryModel {
        val dto = CategoryInMemoryDTO.of(category, UUID.randomUUID().toString())
        return save(dto).toModel()
        //TODO: Check for parent is exist
    }

    override suspend fun rename(id: String, label: String): CategoryModel {
        if (id.isBlank()) throw CategoryRepoWrongIdException(id)
        val dto = cache.get(id)?:throw CategoryRepoNotFoundException(id)
        return save(dto.copy(label = label)).toModel()
    }

    /**
     *  Recursive fun for delete tree of categories by top category id
     *  @return map of categories
     */
    override suspend fun delete(id: String): CategoryModel {
        val model = getWithChildren(id)
        cache.peekAndRemove(id)?: throw CategoryRepoNotFoundException(id)
        //TODO: Need deleting items
        model.children.forEach {
            it.children.add(delete(it.id))
        }
        return model
    }

    private suspend fun save(dto: CategoryInMemoryDTO): CategoryInMemoryDTO {
        cache.put(dto.id, dto)
        return cache.get(dto.id)
    }

    private suspend fun getWithChildren(id: String): CategoryModel{
        if (id.isBlank()) throw CategoryRepoWrongIdException(id)
        val model = cache.get(id)?.toModel()?: throw CategoryRepoNotFoundException(id)
        model.children.addAll(cache.entries()
                ?.filter { it.value.parentId == id }
                ?.map { it.value.toModel() }?: emptyList())

        return model
    }
}