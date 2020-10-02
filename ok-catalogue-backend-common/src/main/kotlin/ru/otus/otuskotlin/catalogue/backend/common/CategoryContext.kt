package ru.otus.otuskotlin.catalogue.backend.common

import ru.otus.otuskotlin.catalogue.backend.common.models.IErrorModel
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.*
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemDeleteStubCases
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
        var stubCGetCase: CategoryGetStubCases = CategoryGetStubCases.NONE,
        var stubCCreateCase: CategoryCreateStubCases = CategoryCreateStubCases.NONE,
        var stubCDeleteCase: CategoryDeleteStubCases = CategoryDeleteStubCases.NONE,
        var stubCGetMapCase: CategoryGetMapStubCases = CategoryGetMapStubCases.NONE,
        var stubCRenameCase: CategoryRenameStubCases = CategoryRenameStubCases.NONE,
        var stubICreateCase: ItemCreateStubCases = ItemCreateStubCases.NONE,
        var stubIDeleteCase: ItemDeleteStubCases = ItemDeleteStubCases.NONE,
        var errors: MutableList<IErrorModel> = mutableListOf(),
        var status: CategoryContextStatus = CategoryContextStatus.NONE
) {

}