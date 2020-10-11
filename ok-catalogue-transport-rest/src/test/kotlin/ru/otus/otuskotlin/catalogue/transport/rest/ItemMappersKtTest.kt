package ru.otus.otuskotlin.catalogue.transport.rest


import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemInfo
import ru.otus.otuskotlin.catalogue.transport.common.models.items.NoteCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.NoteInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


internal class ItemMappersKtTest{

    @Test
    fun itemQueryToModelTest(){
        val query = NoteCreateQuery(
            header = "asd"
        )
        val context = ItemContext()
        context.setQuery(query)
        assertEquals("", context.requestCategoryId)
        assertEquals("asd", context.requestItem.header)
    }

    @Test
    fun itemToDTOTest(){
        val item = NoteModel()
        val dto = item.toDTO()
        assertTrue { dto is NoteInfo }
        assertTrue { dto is ItemInfo }
    }

    @Test
    fun runExceptionUndefinedType(){
        val item = AnotherItem()
        var result:Boolean = true
        try{
            item.toDTO()
        }
        catch (e:Exception){
            result = false
            println(e)
        }
        assertFalse { result }
    }

    private data class AnotherItem(
            override var id: String = ""
    ):ItemModel(
            id = id
    )
}

