package ru.otus.otuskotlin.catalogue.backend.repository.inmemory

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.catalogue.backend.common.dsl.category
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.*
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.IItemRepository
import ru.otus.otuskotlin.catalogue.backend.repository.inmemory.categories.CategoryRepositoryInMemory
import ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items.IItemRepositoryInMemory
import ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items.NoteRepositoryInMemory
import java.lang.ClassCastException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration


internal class CategoryRepositoryInMemoryTest{

    @OptIn(ExperimentalTime::class)
    companion object{
        private val children = mutableSetOf(
            CategoryModel(id = "child-1", parentId = "root", label = "subcategory1"),
            CategoryModel(id = "child-2", parentId = "root", label = "subcategory2")
        )

        private val items = mutableSetOf<ItemModel>(
            NoteModel(id = "delete-item1", categoryId = "get-id", header = "deleted")
        )

        val category1 = CategoryModel(
            id = "root",
            type = "Notes",
            label = "main",
            children = children
        )

        var itemRepo = NoteRepositoryInMemory(
            ttl = 10.toDuration(DurationUnit.MINUTES),
            initObjects = items
        )

        val repo = CategoryRepositoryInMemory(
                ttl = 10.toDuration(DurationUnit.MINUTES),
                initObjects = listOf(
                    category1,
                    CategoryModel(id = "delete-id", label = "deleted", type = "Notes"),
                    CategoryModel(id = "get-id", label = "got", type = "Notes", items = items)
                )
        ).addItemRepository(itemRepo)



    }

    @Test
    fun createTest(){

        runBlocking {
            //val catCreate1 = repo.create(category1)
            val catCreate2 = repo.create(CategoryModel(parentId = "root", label = "cat2"))
            val catCreate3 = repo.create(CategoryModel(parentId = "root", label = "cat3"))
            val catCreate4 = repo.create(CategoryModel(parentId = catCreate3.id, label = "cat4"))
            val catCreate5 = repo.create(CategoryModel(parentId = catCreate4.id, label = "cat5"))
            assertEquals("cat5", catCreate5.label)
        }

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun itemTest(){
        val item1 = NoteModel(id = "test-item-1", header = "first_note")
        val item2 = NoteModel(header = "second_note")
        val item3 = object: ItemModel(id = "test-item-3", header = "third_note"){}
        itemRepo = NoteRepositoryInMemory(
                ttl = 10.toDuration(DurationUnit.MINUTES)
        )
        var message = ""
        runBlocking {
            message = actionWithException { repo.addItem(item1) }
            assertEquals("CategoryRepoWrongIdException", message)

            item1.categoryId = category1.id
            message = actionWithException { repo.addItemRepository(itemRepo).addItem(item1) }
            assertEquals("Successful", message)

            item2.categoryId = category1.id
            message = actionWithException { repo.addItem(item2) }
            assertEquals("ItemRepoWrongIdException", message)

            item2.id = "test-item-2"
            message = actionWithException { repo.addItem(item2) }
            assertEquals("Successful", message)

            item3.categoryId = category1.id
            message = actionWithException { repo.addItem(item3) }
            assertEquals("ClassCastException", message)

            val model = repo.addItemRepository(itemRepo).get("root")
            println(model)
            assertEquals(2, model.items.size)

        }
    }

    @Test
    fun deleteTest(){
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

    @Test
    fun getMapTest(){
        runBlocking{
            val model = repo.create(CategoryModel(parentId = "child-1", label = "child-3"))
            assertEquals("child-3", model.label)

            val tree = repo.getMap("root")
            println(tree)
            assertEquals(1, tree.children.find { it.id == "child-1" }?.children?.size)
        }
    }

    private suspend fun actionWithException(block: suspend () -> Unit): String {
        var message = ""
        try {
            block()
            message = "Successful"
        }
        catch (e: CategoryRepoWrongIdException){
            message = "CategoryRepoWrongIdException"
        }
        catch (e: CategoryRepoNotFoundException){
            message = "CategoryRepoNotFoundException"
        }
        catch (e: ItemRepoWrongIdException){
            message = "ItemRepoWrongIdException"
        }
        catch (e: ItemRepoNotFoundException){
            message = "ItemRepoNotFoundException"
        }
        catch (e: RepoClassNotFoundException){
            message = "RepoClassNotFoundException"
        }
        catch (e: ClassCastException){
            message = "ClassCastException"
        }
        catch (e: Throwable){
            message = "UnhandledException"
        }
        finally {
            println(message)
            return message
        }
    }
}