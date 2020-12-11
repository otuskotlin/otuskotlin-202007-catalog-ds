package ru.otus.otuskotlin.catalogue.backend.repository.inmemory.categories

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.*
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items.IItemRepositoryInMemory
import ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items.NoteRepositoryInMemory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class CategoryRepositoryInMemory @OptIn(ExperimentalTime::class) constructor(
    ttl: Duration,
    initObjects: Collection<CategoryModel> = emptyList(),
    private val itemsRepositories: MutableSet<IItemRepositoryInMemory> = mutableSetOf()
): ICategoryRepositoryInMemory {
    @OptIn(ExperimentalTime::class)
    private var cache: Cache<String, CategoryInMemoryDTO> = object : Cache2kBuilder<String, CategoryInMemoryDTO>() {}
        .expireAfterWrite(ttl.toLongMilliseconds(), TimeUnit.MILLISECONDS) // expire/refresh after 5 minutes
        .suppressExceptions(false)
        .build()
        .also { cache ->
            initObjects.treeToList().forEach {
                cache.put(it.id, CategoryInMemoryDTO.of(it))
            }
        }

    /**
     *  Function for getting a category from the db by [id].
     *  @return a model of category within child categories, items and list of parents thread.
     */
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

    /**
     *  Recursive function for getting a tree of categories from the db by [id] of top category.
     *  @return a model of category within tree of child categories.
     */
    override suspend fun getMap(id: String): CategoryModel {
        val model = getWithChildren(id)
        model.children.forEach {
            it.children.add(getMap(it.id))
        }
        return model
    }

    /**
     * Function that creates a note in the db table by [category]. Also it checks what parent category exists.
     * @return a model of created category from db table.
     */
    override suspend fun create(category: CategoryModel): CategoryModel {
        val dto = CategoryInMemoryDTO.of(category, UUID.randomUUID().toString())
        if (category.parentId.isNotBlank()){
            cache.get(category.parentId)?: throw CategoryRepoNotFoundException(category.parentId)
        }
        return save(dto).toModel()

    }

    /**
     * Function that get a category from db table by [id] and saves it with a new [label].
     * @return a model of saved category.
     */
    override suspend fun rename(id: String, label: String): CategoryModel {
        if (id.isBlank()) throw CategoryRepoWrongIdException(id)
        val dto = cache.get(id)?:throw CategoryRepoNotFoundException(id)
        return save(dto.copy(label = label)).toModel()
    }

    /**
     *  Recursive fun for delete tree of categories by top category [id]
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

    override fun addItemRepository(repository: IItemRepositoryInMemory): ICategoryRepositoryInMemory {
        itemsRepositories.add(repository)
        return this
    }

    /**
     * Function that add new [item] in category. It uses an item repository. Category should have type for type of item.
     * @return a model of item, got via item repository.
     */
    override suspend fun addItem(item: ItemModel): ItemModel {
        if (item.categoryId.isBlank()) throw CategoryRepoWrongIdException(item.categoryId)
        if (item.id.isBlank()) throw ItemRepoWrongIdException(item.id)
        val model = cache.get(item.categoryId)?.toModel()?: throw CategoryRepoNotFoundException(item.categoryId)
        val itemRepo = getRepoByType(model.type)
        val itemResult = itemRepo?.add(item) ?: throw CategoryNotForItemsException(item.categoryId)
        model.items.add(itemResult)
        save(CategoryInMemoryDTO.of(model))
        return itemResult
    }

    /**
     * Function that delete [item] from category table and item table. It uses an item repository.
     * Category should have type for type of item.
     * @return a model of item, got via item repository.
     */
    override suspend fun deleteItem(itemId: String, categoryId: String): ItemModel {
        if (categoryId.isBlank()) throw CategoryRepoWrongIdException(categoryId)
        if (itemId.isBlank()) throw ItemRepoWrongIdException(itemId)
        val model = cache.get(categoryId)?.toModel()?: throw CategoryRepoNotFoundException(categoryId)
        val itemRepo = getRepoByType(model.type)
        val itemResult = itemRepo?.delete(itemId)?: throw CategoryNotForItemsException(categoryId)
        model.items.remove(itemResult)
        save(CategoryInMemoryDTO.of(model))
        return itemResult
    }

    /**
     * Handler for transformation tree of categories to plain list of categories.
     */
    private fun Collection<CategoryModel>.treeToList(): Collection<CategoryModel>{
        val models = this.toMutableList()
        var counter = models.size
        for (i in 0 until counter){
            models.addAll(models[i].children)
            counter += models[i].children.size
        }
        return models
    }

    /**
     * Helping function for save [dto] in category db table.
     * @return a model of saved [dto].
     */
    private suspend fun save(dto: CategoryInMemoryDTO): CategoryInMemoryDTO {
        cache.put(dto.id, dto)
        return cache.get(dto.id)
    }

    /**
     * Fun that get category from db table by [id] within child categories.
     * @return a model of getted category.
     */
    private suspend fun getWithChildren(id: String): CategoryModel{
        if (id.isBlank()) throw CategoryRepoWrongIdException(id)
        val model = cache.get(id)?.toModel()?: throw CategoryRepoNotFoundException(id)
        model.children.addAll(cache.entries()
                ?.filter { it.value.parentId == id }
                ?.map { it.value.toModel() }?: emptyList())

        return model
    }

    /**
     * Fun that @return list of items from item db table by [categoryId].
     */
    private suspend fun getItems(categoryId: String, repository: IItemRepositoryInMemory): Collection<ItemModel>{
        if (categoryId.isBlank()) throw CategoryRepoWrongIdException(categoryId)
        return repository.index(categoryId)?: emptyList()
    }

    /**
     * Fun that get item repository due to [type] of category.
     */
    private  fun getRepoByType(type: String) =
            when(type.toLowerCase()){
                "" -> null
                "notes" -> itemsRepositories.find { it::class == NoteRepositoryInMemory::class }
                        ?: throw RepoClassNotFoundException(NoteRepositoryInMemory::class)
                else -> throw CategoryRepoInvalidTypeException(type)
            }
}