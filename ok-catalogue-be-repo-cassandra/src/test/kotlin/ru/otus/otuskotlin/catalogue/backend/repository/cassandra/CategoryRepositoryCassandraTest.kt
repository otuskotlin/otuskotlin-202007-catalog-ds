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
        private val keyspace = "test_keyspace"
        private lateinit var container: CassandraContainer
        private lateinit var repo: CategoryRepositoryCassandra
        private lateinit var itemRepo: IItemRepositoryCassandra

        @BeforeClass
        @JvmStatic
        fun tearUp(){
            container = CassandraContainer()
                    .withExposedPorts(PORT)
                    .withStartupTimeout(Duration.ofSeconds(120L))
                    .apply {
                        start()
                    }
            val children = mutableSetOf(
                    CategoryModel(id = "child-1", parentId = "root", label = "subcategory1"),
                    CategoryModel(id = "child-2", parentId = "root", label = "subcategory2")
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
                    keySpace = keyspace,
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

    @Test
    fun getWithItemsTest(){
        runBlocking {
            val model = repo.get("delete-id")

            println(model)
            assertEquals("deleted", model.label)
            assertEquals(1, model.items.size)
        }
    }

    @Test
    fun createAndGetMap(){
        runBlocking {
            val model = repo.create(CategoryModel(parentId = "child-1", label = "child-3"))
            assertEquals("child-3", model.label)

            val tree = repo.getMap("root")
            println(tree)
            assertEquals(1, tree.children.find { it.id == "child-1" }?.children?.size)
        }
    }
}