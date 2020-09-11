package ru.otus.otuskotlin.catalogue.backend.common.models

import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import java.lang.Exception
import java.time.LocalDate
import java.util.*

/**
 *  Class used for new category creation
 */
data class CategoryModel(
    var id:String = "",
    var type:String = "",
    var label:String = "",
    var parentId:String = "",
    val parents:MutableList<CategoryModel> = mutableListOf(),
    val children:MutableSet<CategoryModel> = mutableSetOf(),
    val items:MutableSet<ItemModel> = mutableSetOf(),

    val creationDate: LocalDate = LocalDate.EPOCH,
    var modifyDate: LocalDate = LocalDate.EPOCH
) {
//    var children:List<CategoryModel> = emptyList()
//        private set
    init {
        modifyDate = creationDate
    }

    companion object{
        val NONE = CategoryModel()
    }

}