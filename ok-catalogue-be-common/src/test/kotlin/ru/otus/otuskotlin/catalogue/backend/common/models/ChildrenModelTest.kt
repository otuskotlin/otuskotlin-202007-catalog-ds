package ru.otus.otuskotlin.catalogue.backend.common.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class ChildrenModelTest{

    @Test
    fun addEqualObjects(){
        var collection = ChildrenModel<Int>()
        collection.add(5)
        assertFalse(collection.add(5))
        collection.addAll(listOf(2, 3, 5))
        assertEquals(3, collection.size)
        assertEquals(3, collection.last())
        var data = ChildrenModel<Things>()
        data.add(Things(one = 1))
        assertFalse(data.add(Things(one = 1, two = "null")))
        var thing = Things()
        var other = thing
        data.add(thing)
        assertFalse(data.add(other))
    }

    data class Things(
        var one: Int = 0,
        var two: String = "null"
    )
}