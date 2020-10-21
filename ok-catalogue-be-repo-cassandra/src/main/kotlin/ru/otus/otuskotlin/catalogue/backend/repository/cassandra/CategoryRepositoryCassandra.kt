package ru.otus.otuskotlin.catalogue.backend.repository.cassandra

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.await
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.CategoryRepoNotFoundException
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.CategoryRepoWrongIdException
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.CategoryCassandraDTO.Companion.CATEGORY_TABLE_NAME
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.CategoryCassandraDTO.Companion.COLUMN_CREATION_DATE
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.CategoryCassandraDTO.Companion.COLUMN_ID
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.CategoryCassandraDTO.Companion.COLUMN_LABEL
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.CategoryCassandraDTO.Companion.COLUMN_MODIFY_DATE
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.CategoryCassandraDTO.Companion.COLUMN_PARENT_ID
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.CategoryCassandraDTO.Companion.COLUMN_TYPE
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.CategoryCassandraDTO.Companion.of
import java.io.Closeable
import java.net.InetAddress
import java.time.Duration
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
    initObjects: Collection<CategoryModel> = emptyList()
): ICategoryRepository, CoroutineScope, Closeable {

    private val job = Job()

    private val cluster: Cluster by lazy {
        Cluster.builder().addContactPoints(parseAddresses(hosts))
            .withPort(port)
            .withCredentials(user, pass)
            .build()
    }

    private val session: Session by lazy {
        runBlocking {
            createKeySpace()
            cluster.connect(keySpace)
        }
    }

    private val manager: MappingManager by lazy {
        MappingManager(session)
    }

    private val mapper: Mapper<CategoryCassandraDTO> by lazy {
        val mpr = manager.mapper(CategoryCassandraDTO::class.java, keySpace)
        runBlocking {
            initObjects.map {
                withTimeout(timeout.toMillis()){
                    mpr.saveAsync(CategoryCassandraDTO.of(it)).await()
                }
            }
        }
        mpr
    }

    override suspend fun get(id: String): CategoryModel {
        if (id.isBlank()) throw CategoryRepoWrongIdException(id)
        var model: CategoryModel? = null
        val children: MutableSet<CategoryModel> = mutableSetOf()
        val parents: MutableList<CategoryModel> = mutableListOf()
        val items: MutableSet<ItemModel> = mutableSetOf()
        return withTimeout(timeout.toMillis()){
            val modelJob = CoroutineScope(coroutineContext).launch {
                model = mapper.getAsync(id)?.await()?.toModel()?: throw CategoryRepoNotFoundException(id)

                //TODO: Get parents by async
                model?.parents?.addAll(parents)
            }
            val childrenJob = CoroutineScope(coroutineContext).launch {
                //TODO: Get children by async
                modelJob.join()
                model?.children?.addAll(children)
            }
            val itemsJob = CoroutineScope(coroutineContext).launch {
                //TODO: Get items by async
                modelJob.join()
                model?.items?.addAll(items)
            }

            model?: throw CategoryRepoNotFoundException(id)
        }

    }

    override suspend fun getMap(id: String): CategoryModel {
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

    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")

    override fun close() {
        TODO("Not yet implemented")
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
               $COLUMN_CREATION_DATE date,
               $COLUMN_MODIFY_DATE date,

               PRIMARY KEY ($COLUMN_ID)
            )
       """.trimIndent()).await()
    }
}