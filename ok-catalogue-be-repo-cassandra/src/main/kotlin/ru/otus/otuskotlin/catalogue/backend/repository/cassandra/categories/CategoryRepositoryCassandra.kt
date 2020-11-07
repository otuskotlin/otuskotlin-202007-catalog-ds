package ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.CodecRegistry
import com.datastax.driver.core.Session
import com.datastax.driver.core.TypeCodec
import com.datastax.driver.extras.codecs.jdk8.LocalDateCodec
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.await
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.*
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.CATEGORY_TABLE_NAME
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.COLUMN_CHILDREN_ID
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.COLUMN_CREATION_DATE
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.COLUMN_ID
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.COLUMN_ITEMS_ID
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.COLUMN_LABEL
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.COLUMN_MODIFY_DATE
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.COLUMN_PARENT_ID
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.COLUMN_TYPE
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.of
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.IItemRepositoryCassandra
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteRepositoryCassandra
import java.net.InetAddress
import java.time.Duration
import java.util.*
import kotlin.coroutines.CoroutineContext

class CategoryRepositoryCassandra (
    private val keySpace: String,
    hosts: String = "",
    port: Int = 9042,
    user: String = "cassandra",
    pass: String = "cassandra",
    private val timeout: Duration = Duration.ofSeconds(10),
    private val searchParallelism: Int = 1,
    private val replicationFactor: Int = 1,
    initObjects: Collection<CategoryModel> = emptyList(),
    private val itemRepositories: MutableSet<IItemRepositoryCassandra> = mutableSetOf()
): ICategoryRepositoryCassandra {

    private val job = Job()

    private val cluster: Cluster by lazy {
        Cluster.builder().addContactPoints(parseAddresses(hosts))
            .withPort(port)
            .withCredentials(user, pass)
                .withCodecRegistry(CodecRegistry().register(TypeCodec.set(TypeCodec.varchar())))
                .withCodecRegistry(CodecRegistry().register(LocalDateCodec.instance))
            .build()
    }

    private val session: Session by lazy {
        runBlocking {
            createKeySpace()
            cluster.connect(keySpace).apply { createTable() }
        }
    }

    private val manager: MappingManager by lazy {
        MappingManager(session)
    }

    private val mapper: Mapper<CategoryCassandraDTO> by lazy {
        val mpr = manager.mapper(CategoryCassandraDTO::class.java, keySpace)
        runBlocking {
            initObjects.treeToList().map {
                withTimeout(timeout.toMillis()){
                    mpr.saveAsync(of(it)).await()
                }
            }
        }
        mpr
    }

    /**
     *  Asynchronous function for getting a category from the db by [id].
     *  @return a model of category within child categories, items and list of parents thread.
     */
    override suspend fun get(id: String): CategoryModel {
        if (id.isBlank()) throw CategoryRepoWrongIdException(id)
        return withTimeout(timeout.toMillis()){
            val modelCassandra = mapper.getAsync(id)?.await()?: throw CategoryRepoNotFoundException(id)
            val model = modelCassandra.toModel()
            val parentsJob = CoroutineScope(coroutineContext).launch {
                var parentId = model.parentId
                while (!parentId.isBlank()){
                    val parent = mapper.getAsync(parentId)?.await()?.toModel()?: throw CategoryRepoNotFoundException(parentId)
                    model.parents.add(parent)
                    parentId = parent.parentId
                }
            }
            val childrenJob: MutableList<Job> = mutableListOf()
                modelCassandra.children?.forEach {
                    childrenJob.add(
                            CoroutineScope(coroutineContext).launch {
                                val child = mapper.getAsync(it)?.await()?.toModel()?: throw CategoryRepoNotFoundException(it)
                                model.children.add(child)
                            }
                    )
                }
            //println("childrenJobs=${childrenJob.size}")

            val itemsJob: MutableList<Job> = mutableListOf()
            val itemRepo = getRepoByType(model.type)
            itemRepo?.let {
                modelCassandra.items?.forEach {
                    itemsJob.add(
                            CoroutineScope(coroutineContext).launch {
                                model.items.add(itemRepo.get(it))
                            }
                    )
                }
            }

            parentsJob.join()
            childrenJob.joinAll()
            itemsJob.joinAll()
            model
        }

    }

    /**
     *  Asynchronous recursive function for getting a tree of categories from the db by [id] of top category.
     *  @return a model of category within tree of child categories.
     */
    override suspend fun getMap(id: String): CategoryModel {
        if (id.isBlank()) throw CategoryRepoWrongIdException(id)
        return withTimeout(timeout.toMillis()){
            val modelCassandra = mapper.getAsync(id)?.await() ?: throw CategoryRepoNotFoundException(id)
            val model = modelCassandra.toModel()
            val jobs: MutableList<Job> = mutableListOf()
            modelCassandra.children?.forEach {
                  val job = CoroutineScope(coroutineContext).launch {
                    model.children.add(getMap(it))
                 }
                 jobs.add(job)
            }
            jobs.joinAll()
            model
        }
    }

    /**
     * Function that creates a note in the db table by [category]. Also it adds a children in parent category if exists.
     * @return a model of created category from db table.
     */
    override suspend fun create(category: CategoryModel): CategoryModel {
        val id = UUID.randomUUID().toString()
        val dto = of(category, id)
        val model = save(dto)
        if (category.parentId.isNotBlank()){
            val parent = get(category.parentId)
            parent.children.add(CategoryModel(id = id))
            save(of(parent))
        }
        return model
    }

    /**
     * Function that get a category from db table by [id] and saves it with a new [label].
     * @return a model of saved category.
     */
    override suspend fun rename(id: String, label: String): CategoryModel {
        if (id.isBlank()) throw CategoryRepoWrongIdException(id)
        val dto = withTimeout(timeout.toMillis()){
            mapper.getAsync(id)?.await()?: throw CategoryRepoNotFoundException(id)}
        return save(dto.copy(label = label))
    }

    /**
     *  Function for delete tree of categories by top category [id]
     *  @return map of categories
     */
    override suspend fun delete(id: String): CategoryModel = deleteTree(id, true)

    /**
     * Function that add new [item] in category table and item table. It uses an item repository.
     * Category should have type is equaled type of item.
     * @return a model of item, got via item repository.
     */
    override suspend fun addItem(item: ItemModel): ItemModel {
        if (item.categoryId.isBlank()) throw CategoryRepoWrongIdException(item.categoryId)
        if (item.id.isBlank()) throw ItemRepoWrongIdException(item.id)
        return withTimeout(timeout.toMillis()){
            val modelCassandra = mapper.getAsync(item.categoryId)?.await()?: throw CategoryRepoNotFoundException(item.categoryId)
            val model = modelCassandra.toModel()
            val itemRepo = getRepoByType(model.type)
            val itemResult = itemRepo?.add(item)?: throw CategoryNotForItemsException(item.categoryId)
            model.items.add(itemResult)
            mapper.saveAsync(of(model)).await()
            itemResult
        }
    }

    /**
     * Function that delete [item] from category table and item table. It uses an item repository.
     * Category should have type for type of item.
     * @return a model of item, got via item repository.
     */
    override suspend fun deleteItem(itemId: String, categoryId: String): ItemModel {
        if (categoryId.isBlank()) throw CategoryRepoWrongIdException(categoryId)
        if (itemId.isBlank()) throw ItemRepoWrongIdException(itemId)
        return withTimeout(timeout.toMillis()){
            val modelCassandra = mapper.getAsync(categoryId)?.await()?: throw CategoryRepoNotFoundException(categoryId)
            val model = modelCassandra.toModel()
            val itemRepo = getRepoByType(model.type)
            val itemResult = itemRepo?.delete(itemId)?: throw CategoryNotForItemsException(categoryId)
            model.items.remove(itemResult)
            mapper.saveAsync(of(model)).await()
            itemResult
        }
    }

    /**
     * Asynchronous recursive function that deletes tree of child categories and items by top category [id].
     * [needRemoveFromParent] is false for all inner calls, because parent category is already deleted.
     * @return map of deleted categories.
     */
    private suspend fun deleteTree(id: String, needRemoveFromParent: Boolean): CategoryModel {
        if (id.isBlank()) throw CategoryRepoWrongIdException(id)
        return withTimeout(timeout.toMillis()){
            val modelCassandra = mapper.getAsync(id)?.await()?: throw CategoryRepoNotFoundException(id)
            val model = modelCassandra.toModel()

            // remove id from parent's children set
            if (needRemoveFromParent){
                if (model.parentId.isNotBlank())
                CoroutineScope(coroutineContext).launch {
                    val parent = mapper.getAsync(model.parentId)?.await()
                    parent?.let{
                        val children = parent.children?.toMutableSet()
                        children?.let {
                            it.remove(id)
                            mapper.saveAsync(parent.copy(children = children))
                        }
                    }
                }
            }

            CoroutineScope(coroutineContext).launch {
                mapper.deleteAsync(modelCassandra).await()
            }

            // delete items branch
            val itemRepo = getRepoByType(model.type)
            val itemJobs: MutableList<Job> = mutableListOf()
            itemRepo?.let {
                modelCassandra.items?.forEach {
                    itemJobs.add(
                            CoroutineScope(coroutineContext).launch {
                                itemRepo.delete(it)
                            }
                    )
                }
            }

            val jobs: MutableList<Job> = mutableListOf()
            modelCassandra.children?.forEach {
                val job = CoroutineScope(coroutineContext).launch {
                    model.children.add(deleteTree(it, false))
                }
                jobs.add(job)
            }
            itemJobs.joinAll()
            jobs.joinAll()
            model
        }
    }

    /**
     * Helping function for save [dto] in category db table.
     * @return a model of saved [dto].
     */
    private suspend fun save(dto: CategoryCassandraDTO): CategoryModel{
        withTimeout(timeout.toMillis()){ mapper.saveAsync(dto).await()}
        return get(dto.id?:"")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun close() {
        itemRepositories.forEach {
                it.close()
        }
        job.cancel()
    }

    /**
     * Fun that get item repository due to [type] of category.
     */
    private  fun getRepoByType(type: String) =
            when(type.toLowerCase()){
                "" -> null
                "notes" -> itemRepositories.find { it::class == NoteRepositoryCassandra::class }
                        ?: throw RepoClassNotFoundException(NoteRepositoryCassandra::class)
                else -> throw CategoryRepoInvalidTypeException(type)
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

    private fun parseAddresses(hosts: String): Collection<InetAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetAddress.getByName(it) }

    private suspend fun createKeySpace() {
        cluster
            .connect()
                //@TODO: learn about strategy
            .executeAsync("""
            CREATE KEYSPACE IF NOT EXISTS $keySpace WITH REPLICATION = { 
                'class' : 'SimpleStrategy', 
                'replication_factor' : $replicationFactor
            }
        """.trimIndent()).await()
    }

    private suspend fun Session.createTable() {
        executeAsync("""
            CREATE TABLE IF NOT EXISTS $CATEGORY_TABLE_NAME (
               $COLUMN_ID text,
               $COLUMN_TYPE text,
               $COLUMN_LABEL text,
               $COLUMN_PARENT_ID text,
               $COLUMN_CHILDREN_ID set <text>,
               $COLUMN_ITEMS_ID set <text>,
               $COLUMN_CREATION_DATE date,
               $COLUMN_MODIFY_DATE date,

               PRIMARY KEY ($COLUMN_ID)
            )
       """.trimIndent()).await()

    }

    // init lazy fields
    override fun init(): ICategoryRepositoryCassandra {
        val mapper = mapper
        itemRepositories.forEach {
            it.init()
        }
        return this
    }

    override fun addItemRepository(repository: IItemRepositoryCassandra): ICategoryRepositoryCassandra {
        itemRepositories.add(repository)
        return this
    }
}