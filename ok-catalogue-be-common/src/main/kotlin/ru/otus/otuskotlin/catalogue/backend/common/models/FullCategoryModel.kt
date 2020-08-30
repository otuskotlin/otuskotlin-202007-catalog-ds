package ru.otus.otuskotlin.catalogue.backend.common.models

import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import java.time.LocalDate

/**
 * Class, used for return category with taxonomy
 */
data class FullCategoryModel(
        var id:String = "",
        var type:CategoryType = CategoryType.NONE,
        var label:String = "",
        val parents:MutableList<CategoryInfoModel> = mutableListOf(),
        val children:MutableList<CategoryInfoModel> = mutableListOf(),
        var items:MutableList<ItemModel> = mutableListOf(),
        val creationDate: LocalDate = LocalDate.EPOCH,
        var modifyDate: LocalDate = LocalDate.EPOCH
) {
    companion object{
        val NONE = FullCategoryModel()
    }
}