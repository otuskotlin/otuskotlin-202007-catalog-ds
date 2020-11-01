package ru.otus.otuskotlin.catalogue.backend.repository.cassandra

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.GenericContainer
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryRepositoryCassandra
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.IItemRepositoryCassandra
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items.NoteRepositoryCassandra
import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals

class CassandraContainer: GenericContainer<CassandraContainer>("cassandra")

internal class CategoryRepositoryCassandraTest{

    companion object{
        private val PORT = 9042
        private val keyspace = "categories"
        private val itemKeyspace = "item_test_keyspace"
        private lateinit var container: CassandraContainer
        private lateinit var repo: CategoryRepositoryCassandra
        private lateinit var itemRepo: IItemRepositoryCassandra

        @BeforeClass
        @JvmStatic
        fun tearUp(){
            container = CassandraContainer()
                    .withExposedPorts(PORT)
                    .withStartupTimeout(Duration.ofSeconds(40L))
                    .apply {
                        start()
                    }
            val children = mutableSetOf(
                    CategoryModel(id = "child-1", label = "subcategory1"),
                    CategoryModel(id = "child-2", label = "subcategory2")
            )

            val items = mutableSetOf<ItemModel>(
                    NoteModel(id = "delete-item1", categoryId = "delete-id", header = "deleted")
            )

            repo = CategoryRepositoryCassandra(
                    keySpace = keyspace,
                    hosts = container.host,
                    port = container.getMappedPort(PORT),
                    initObjects = listOf(
                            CategoryModel(id = "root", label = "main", type = "NoTes", children = children),
                            CategoryModel(id = "delete-id", label = "deleted", type = "Notes", items = items)
                    )
            )

            itemRepo = NoteRepositoryCassandra(
                    keySpace = itemKeyspace,
                    hosts = container.host,
                    port = container.getMappedPort(PORT),
                    initObjects = items.toList()
            )

            repo.addItemRepository(itemRepo).init()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            container.close()
        }
    }

    @Test
    fun getTest(){
        runBlocking {
            val model = repo.get("root")

            println(model)
            assertEquals("main", model.label)
            assertEquals(2, model.children.size)
        }
    }
}