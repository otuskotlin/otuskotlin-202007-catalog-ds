package ru.otus.otuskotlin.catalogue.backend.common.models

import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import java.lang.Exception
import java.time.LocalDate
import java.util.*


data class CategoryModel(
    var id:String = "",
    var type:CategoryType = CategoryType.NONE,
    var label:String = "",
    var parentId:String = "",
    val children:ChildrenModel<CategoryModel> = ChildrenModel(),
    var items:MutableList<ItemModel> = mutableListOf(),
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

    override fun equals(other: Any?): Boolean {
        if(other is CategoryModel){
            return (other.parentId == this.parentId) &&
                    (other.label == this.label)
        }
        return super.equals(other)
    }

    /*fun addChild(
        _type: String = "",
        _label: String = ""
    ) {
        val child = CategoryModel(
            type = _type,
            label = _label,
            parent = this
        )
        children.forEach {
            if (it == child) return
        }

        val mediator = children.toMutableList()
        mediator.add(child)
        children = mediator.toList()
    }*/
}