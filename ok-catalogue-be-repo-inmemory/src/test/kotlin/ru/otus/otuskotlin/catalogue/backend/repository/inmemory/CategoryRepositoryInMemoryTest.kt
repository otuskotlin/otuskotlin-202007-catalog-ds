package ru.otus.otuskotlin.catalogue.backend.repository.inmemory

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.catalogue.backend.common.dsl.category
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration


internal class CategoryRepositoryInMemoryTest{

    @OptIn(ExperimentalTime::class)
    @Test
    fun baseTest(){
        val repo = CategoryRepositoryInMemory(
                ttl = 10.toDuration(DurationUnit.MINUTES)
        )

        val category1 = category {
            name {
                label = "cat1"
                type = "note"
            }
        }

        runBlocking {
            val catCreate1 = repo.create(category1)
            val catCreate2 = repo.create(catCreate1.copy(parentId = catCreate1.id))
            val catCreate3 = repo.create(catCreate1.copy(parentId = catCreate1.id))
            val catCreate4 = repo.create(catCreate1.copy(parentId = catCreate3.id))
            val catCreate5 = repo.create(catCreate1.copy(parentId = catCreate4.id))
            assertEquals("cat1", catCreate5.label)
            assertEquals("note", catCreate5.type)

            val catGet = repo.get(catCreate5.id)
            assertEquals(3, catGet.parents.size)
            assertEquals(catCreate4.id, catGet.parentId)
            val catGetMap = repo.getMap(catCreate1.id)
            assertEquals(2, catGetMap.children.size)
            println(catGet)
            println(catGetMap)
        }

    }
}