package ru.otus.otuskotlin.catalogue.backend.repository.inmemory.categories

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.CategoryRepoInvalidTypeException
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.CategoryRepoNotFoundException
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.CategoryRepoWrongIdException
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.ItemRepoWromgIdException
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import ru.otus.otuskotlin.catalogue.backend.common.repositories.IItemRepository
import ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items.NoteRepositoryInMemory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class CategoryRepositoryInMemory @OptIn(ExperimentalTime::class) constructor(
    ttl: Duration,
    initObjects: Collection<CategoryModel> = emptyList(),
    private val itemsRepositories: MutableSet<IItemRepository> = mutableSetOf()
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
        val itemRepo = getRepoByType(model.type)
        itemRepo?.let {
            model.items.addAll(getItems(id, it))
        }
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
        if (category.parentId.isNotBlank()){
            cache.get(category.parentId)?: throw CategoryRepoNotFoundException(category.parentId)
        }
        return save(dto).toModel()

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

        val itemRepo = getRepoByType(model.type)
        itemRepo?.let {repo ->
            val itemList = getItems(id, repo).map { it.id }
            itemList.forEach {itemId ->
                repo.delete(itemId)
            }
        }

        model.children.forEach {
            it.children.add(delete(it.id))
        }
        return model
    }

    override suspend fun addItemRepository(repository: IItemRepository): ICategoryRepository {
        itemsRepositories.add(repository)
        return this
    }

    override suspend fun addItem(item: ItemModel): ItemModel {
        if (item.categoryId.isBlank()) throw CategoryRepoWrongIdException(item.categoryId)
        if (item.id.isBlank()) throw ItemRepoWromgIdException(item.id)
        val model = cache.get(item.categoryId)?.toModel()?: throw CategoryRepoNotFoundException(item.categoryId)
        val itemRepo = getRepoByType(model.type)
        val itemResult = itemRepo?.add(item) ?: throw Exception("Category type should not be empty.")
        model.items.add(itemResult)
        save(CategoryInMemoryDTO.of(model))
        return itemResult
    }

    override suspend fun deleteItem(itemId: String, categoryId: String): ItemModel {
        if (categoryId.isBlank()) throw CategoryRepoWrongIdException(categoryId)
        if (itemId.isBlank()) throw ItemRepoWromgIdException(itemId)
        val model = cache.get(categoryId)?.toModel()?: throw CategoryRepoNotFoundException(categoryId)
        val itemRepo = getRepoByType(model.type)
        val itemResult = itemRepo?.delete(itemId)?: throw Exception("Category type should not be empty.")
        model.items.remove(itemResult)
        save(CategoryInMemoryDTO.of(model))
        return itemResult
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

    private suspend fun getItems(categoryId: String, repository: IItemRepository): Collection<ItemModel>{
        if (categoryId.isBlank()) throw CategoryRepoWrongIdException(categoryId)
        return repository.index(categoryId)?: emptyList()
    }

    private  fun getRepoByType(type: String) =
            when(type){
                "" -> null
                "notes" -> itemsRepositories.find { it::class == NoteRepositoryInMemory::class }
                else -> throw CategoryRepoInvalidTypeException(type)
            }
}