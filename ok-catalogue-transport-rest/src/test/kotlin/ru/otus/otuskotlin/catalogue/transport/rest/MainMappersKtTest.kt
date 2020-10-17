package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryType
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel
import ru.otus.otuskotlin.catalogue.transport.common.models.ErrorDTO
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemInfo
import ru.otus.otuskotlin.catalogue.transport.common.models.items.NoteInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class MainMappersKtTest{

//    @Test
//    fun checkTypeForCreateModel(){
//        var context = CategoryContext()
//        var create = CategoryCreateQuery(
//            type = "null"
//        )
//        assertEquals(context.setQuery(create).reguestCategory.type, CategoryType.NONE)
//        create.type = "NoTes"
//        assertEquals(context.setQuery(create).reguestCategory.type, CategoryType.NOTES)
//    }


    @Test
    fun `is note asserts item to generic`(){
        var model = CategoryModel()
        model.type = CategoryType.NOTES.toString()
        model.items.add(NoteModel())
        var dto = model.toDTO()
        assertTrue { dto.itemList!!.first() is NoteInfo }
        assertTrue { dto.itemList!!.first() is ItemInfo }
    }

    @Test
    fun treeCheck(){
        var item = CategoryModel()
        for(i in 0..3){
            item.children.add(CategoryModel(
                    id = i.toString()
            ))
        }
        item.children.last().children.add(CategoryModel(
                id = "last"
        ))
        var dto = item.toTreeDTO()
        println(dto)
        assertEquals(4, dto.children?.size)
        assertEquals("last", dto.children?.last()?.children?.last()?.id)
    }

//    @Test
//    fun treeCheck(){
//        var item = CategoryInfoModel()
//        for(i in 0..3){
//            item.tree.add(CategoryInfoModel(
//                    id = i.toString()
//            ))
//        }
//        item.tree.last().tree.add(CategoryInfoModel(
//                id = "last"
//        ))
//        var dto = item.toTreeDTO()
//        println(dto)
//        assertEquals(4, dto.children?.size)
//        assertEquals("last", dto.children?.last()?.children?.last()?.id)
//    }
}