package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryType
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryCreateQuery
import kotlin.test.Test
import kotlin.test.assertEquals


internal class MainMappersKtTest{

    @Test
    fun checkTypeForCreateModel(){
        var context = CategoryContext()
        var create = CategoryCreateQuery(
            type = "null"
        )
        assertEquals(context.setQuery(create).reguestCategory.type, CategoryType.NONE)
        create.type = "NoTes"
        assertEquals(context.setQuery(create).reguestCategory.type, CategoryType.NOTES)
    }
}