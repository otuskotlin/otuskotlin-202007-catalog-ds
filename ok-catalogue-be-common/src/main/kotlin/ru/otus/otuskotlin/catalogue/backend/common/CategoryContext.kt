package ru.otus.otuskotlin.catalogue.backend.common

import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryInfoModel
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel


/**
 *  Стоит ли контексты категории и итема держать в одном контексте?
 */
data class CategoryContext(
        var requestCategoryId: String = "",
        var requestItemId: String = "",
        var requestLabel: String = "",
        var reguestCategory: CategoryModel = CategoryModel.NONE,
        var responseCategory: CategoryModel = CategoryModel.NONE,
        var responseMap: CategoryInfoModel = CategoryInfoModel.NONE,
        var requestItem: ItemModel = ItemModel.NONE,
        var status: CategoryContextStatus = CategoryContextStatus.NONE
)