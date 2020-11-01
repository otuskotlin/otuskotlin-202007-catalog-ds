package ru.otus.otuskotlin.catalogue.backend.repository.inmemory

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.catalogue.backend.common.dsl.category
import ru.otus.otuskotlin.catalogue.backend.common.exceptions.*
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
        val category1 = category {
            id = "root"
            name {
                label = "cat1"
                type = "notes"
            }
        }

        val repo = CategoryRepositoryInMemory(
                ttl = 10.toDuration(DurationUnit.MINUTES),
                initObjects = listOf(category1)
        )

        lateinit var itemRepo: IItemRepositoryInMemory


    }

    @Test
    fun createTest(){

        runBlocking {
            //val catCreate1 = repo.create(category1)
            val catCreate2 = repo.create(category1.copy(parentId = category1.id))
            val catCreate3 = repo.create(category1.copy(parentId = category1.id))
            val catCreate4 = repo.create(category1.copy(parentId = catCreate3.id))
            val catCreate5 = repo.create(category1.copy(parentId = catCreate4.id))
            assertEquals("cat1", catCreate5.label)
            assertEquals("notes", catCreate5.type)

//            val catGet = repo.get(catCreate5.id)
//            assertEquals(3, catGet.parents.size)
//            assertEquals(catCreate4.id, catGet.parentId)
//            val catGetMap = repo.getMap(category1.id)
//            assertEquals(2, catGetMap.children.size)
//            println(catGet)
//            println(catGetMap)
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
            message = actionWithException { repo.addItem(item1) }
            assertEquals("RepoClassNotFoundException", message)

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