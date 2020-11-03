package ru.otus.otuskotlin.catalogue.backend.repository.cassandra

import kotlinx.coroutines.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.GenericContainer
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.CategoryRepoNotFoundException
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
                    .withStartupTimeout(Duration.ofSeconds(60L))
                    .apply {
                        start()
                    }
            val children = mutableSetOf(
                    CategoryModel(id = "child-1", parentId = "root", label = "subcategory1"),
                    CategoryModel(id = "child-2", parentId = "root", label = "subcategory2")
            )

            val items = mutableSetOf<ItemModel>(
                    NoteModel(id = "delete-item1", categoryId = "get-id", header = "deleted")
            )

            repo = CategoryRepositoryCassandra(
                    keySpace = keyspace,
                    hosts = container.host,
                    port = container.getMappedPort(PORT),
                    initObjects = listOf(
                            CategoryModel(id = "root", label = "main", type = "NoTes", children = children),
                            CategoryModel(id = "delete-id", label = "deleted", type = "Notes"),
                            CategoryModel(id = "get-id", label = "got", type = "Notes", items = items)
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
            val model = repo.get("get-id")

            println(model)
            assertEquals("got", model.label)
            assertEquals(1, model.items.size)
        }
    }

    @Test
    fun createAndGetMapTest(){
        runBlocking{
                val model = repo.create(CategoryModel(parentId = "child-1", label = "child-3"))
                assertEquals("child-3", model.label)

                val tree = repo.getMap("root")
                println(tree)
                assertEquals(1, tree.children.find { it.id == "child-1" }?.children?.size)
            }
    }

    @Test
    fun createAndDeleteTest(){
        runBlocking {
            repo.create(CategoryModel(parentId = "delete-id", label = "child-4"))
            repo.create(CategoryModel(parentId = "delete-id", label = "child-5"))
            repo.create(CategoryModel(parentId = "delete-id", label = "child-6"))
            val modelGet = repo.get("delete-id")
            val childId = modelGet.children.find { it.label == "child-5" }?.id?:""
            assertEquals(3, modelGet.children.size)
            val model = repo.delete("delete-id")
            println(model)
            assertEquals("deleted", model.label)
            var message = ""
            try {
                repo.get("delete-id")
            }
            catch (e: CategoryRepoNotFoundException){
                message += "Parent category not found, "
            }
            try {
                repo.get(childId)
            }
            catch (e: CategoryRepoNotFoundException){
                message += "Child category not found."
            }
            assertEquals("Parent category not found, Child category not found.", message)
        }
    }

    @Test
    fun addItemTest(){
        runBlocking {
            val item = repo.addItem(NoteModel(id = "note", categoryId = "get-id", preview = "some text"))
            println(item)
            assertEquals("some text", (item as NoteModel).preview)
        }
    }

    @Test
    fun deleteItemTest(){
        runBlocking {
            val item = repo.deleteItem(itemId = "delete-item1", categoryId = "get-id")
            println(item)
            assertEquals("deleted", item.header)
            val model = repo.get("get-id")
            val itemDeleted = model.items.find { it.id == "delete-item1" }
            assertEquals(null, itemDeleted)
        }
    }
}