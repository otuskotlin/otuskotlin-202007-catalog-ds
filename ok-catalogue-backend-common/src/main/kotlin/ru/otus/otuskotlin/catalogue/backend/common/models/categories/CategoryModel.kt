package ru.otus.otuskotlin.catalogue.backend.common.models.categories

import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import java.time.LocalDate

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

        val creationDate: LocalDate = LocalDate.MIN,
        var modifyDate: LocalDate = creationDate
) {
//    var children:List<CategoryModel> = emptyList()
//        private set

    companion object{
        val NONE = CategoryModel()
    }

}