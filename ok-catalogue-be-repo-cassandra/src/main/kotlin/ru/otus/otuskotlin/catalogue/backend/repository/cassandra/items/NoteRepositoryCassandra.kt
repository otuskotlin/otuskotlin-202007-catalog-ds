package ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.await
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.ItemRepoNotFoundException
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.ItemRepoWrongIdException
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.IItemRepository
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteCassandraDTO.Companion.COLUMN_CATEGORY_ID
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteCassandraDTO.Companion.COLUMN_DESCRIPTION
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteCassandraDTO.Companion.COLUMN_HEADER
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteCassandraDTO.Companion.COLUMN_ID
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteCassandraDTO.Companion.COLUMN_PREVIEW
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteCassandraDTO.Companion.ITEM_TABLE_NAME
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteCassandraDTO.Companion.of
import java.io.Closeable
import java.net.InetAddress
import java.time.Duration
import kotlin.coroutines.CoroutineContext

class NoteRepositoryCassandra(
        private val keySpace: String,
        hosts: String = "",
        port: Int = 9042,
        user: String = "cassandra",
        pass: String = "cassandra",
        private val timeout: Duration = Duration.ofSeconds(10),
        private val searchParallelism: Int = 1,
        private val replicationFactor: Int = 1,
        initObjects: Collection<ItemModel> = emptyList()
): IItemRepositoryCassandra {

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
            cluster.connect(keySpace).apply { createTable() }
        }
    }

    private val manager: MappingManager by lazy {
        MappingManager(session)
    }

    private val mapper: Mapper<NoteCassandraDTO> by lazy {
        val mpr = manager.mapper(NoteCassandraDTO::class.java, keySpace)
        runBlocking {
            initObjects.filterIsInstance<NoteModel>()
                    .map {
                withTimeout(timeout.toMillis()){
                    mpr.saveAsync(NoteCassandraDTO.of(it)).await()
                }
            }
        }
        mpr
    }

    override suspend fun add(item: ItemModel): ItemModel {
        if (item is NoteModel){
            val dto = of(item)
            return save(dto).toModel()
        }
        throw ClassCastException("Expected: NoteModel, Found: ${item::class}")
    }

    override suspend fun delete(id: String): ItemModel {
        if (id.isBlank()) throw ItemRepoWrongIdException(id)
        return withTimeout(timeout.toMillis()){
            val item = mapper.getAsync(id).await()
            item?.let{
                mapper.deleteAsync(it).await()
                return@withTimeout it.toModel()
            }
            throw ItemRepoNotFoundException(id)
        }
    }

    override suspend fun get(id: String): ItemModel {
        if (id.isBlank()) throw ItemRepoWrongIdException(id)
        return withTimeout(timeout.toMillis()){
            mapper.getAsync(id)?.await()?.toModel()?:throw ItemRepoNotFoundException(id)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun close() {
        job.cancel()
    }

    private suspend fun save(dto: NoteCassandraDTO): NoteCassandraDTO {
        withTimeout(timeout.toMillis()){ mapper.saveAsync(dto).await()}
        return withTimeout(timeout.toMillis()){ mapper.getAsync(dto.id).await()}
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
            CREATE TABLE IF NOT EXISTS $ITEM_TABLE_NAME (
                $COLUMN_ID = text,
                $COLUMN_CATEGORY_ID = text,
                $COLUMN_HEADER = text,
                $COLUMN_DESCRIPTION = text,
                $COLUMN_PREVIEW = text,

               PRIMARY KEY ($COLUMN_ID)
            )
       """.trimIndent()).await()

    }

    override fun init(): IItemRepositoryCassandra {
        val mapper = mapper
        return this
    }
}