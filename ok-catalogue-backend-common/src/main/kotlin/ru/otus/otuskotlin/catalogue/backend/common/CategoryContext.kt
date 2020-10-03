package ru.otus.otuskotlin.catalogue.backend.common

import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryInfoModel
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel


data class CategoryContext(
        var requestCategoryId: String = "",
        var requestItemId: String = "",
        var requestLabel: String = "",
        var requestCategory: CategoryModel = CategoryModel.NONE,
        var responseCategory: CategoryModel = CategoryModel.NONE,
        var responseMap: CategoryInfoModel = CategoryInfoModel.NONE,
        var requestItem: ItemModel = ItemModel.NONE,
        var responseItem: ItemModel = ItemModel.NONE,
        var status: CategoryContextStatus = CategoryContextStatus.NONE
)