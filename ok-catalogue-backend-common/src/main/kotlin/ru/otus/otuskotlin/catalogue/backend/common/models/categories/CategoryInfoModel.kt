package ru.otus.otuskotlin.catalogue.backend.common.models.categories

/**
 *  Deprecated, include in CategoryModel
 *  Tree of catalogue structure
 */
data class CategoryInfoModel(
        var id: String = "",
        var label: String = "",
        var tree: MutableList<CategoryInfoModel> = mutableListOf()
) {
    companion object{
        val NONE = CategoryInfoModel()
    }
}