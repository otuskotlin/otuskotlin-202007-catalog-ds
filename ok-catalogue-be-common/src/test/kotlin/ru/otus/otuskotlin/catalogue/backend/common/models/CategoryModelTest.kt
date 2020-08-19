package ru.otus.otuskotlin.catalogue.backend.common.models

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals


internal class CategoryModelTest{

    @Test
    fun createCategory(){
        val cat = CategoryModel(
            type = "feed",
            creationDate = LocalDate.now()
        )
        cat.label = "News"
        assertEquals("feed", cat.type)
        assertEquals("News", cat.label)
        assertEquals(null, cat.parent)
        assertEquals(LocalDate.now(), cat.modifyDate)
    }

    @Test
    fun `are children equal`(){
        val pets = CategoryModel(
            type = "Pets"
        )
        pets.children.add(
            CategoryModel(label = "Cat")
        )
        pets.children.add(
            CategoryModel(label = "Cat")
        )
        pets.children.add(
                CategoryModel(label = "Dog")
                )
        assertEquals(2, pets.children.size)
    }
}