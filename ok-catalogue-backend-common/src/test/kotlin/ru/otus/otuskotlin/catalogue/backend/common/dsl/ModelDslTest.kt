package ru.otus.otuskotlin.catalogue.backend.common.dsl

import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class ModelDslTest {

    @Test
    fun createCategoryDslTest(){
        val category: CategoryModel = category{
            name {
                label = "Some"
            }
            id = "1234"
            type = "Note"
            parents{
                parentId = "12345"
                add(category {  })
                + category {
                    name { label = "Second" }
                }
            }
            children{
                add(category {
                    name { label = "Fourth" } })
                + category { }
            }
            items {
                add(NoteModel(header = "Note"))
            }
            date {
                creation = LocalDate.parse("2020-02-20")
            }
        }
        assertEquals("Some", category.label)
        assertEquals(4, category.id.length)
        assertEquals("Note", category.type)
        assertEquals("", category.parents[0].id)
        assertEquals(2, category.children.size)
        assertTrue { category.items.contains(NoteModel(header = "Note")) }

    }




}