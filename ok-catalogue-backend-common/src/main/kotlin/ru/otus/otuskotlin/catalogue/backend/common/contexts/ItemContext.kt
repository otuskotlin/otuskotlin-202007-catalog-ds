package ru.otus.otuskotlin.catalogue.backend.common.contexts

import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemDeleteStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel

data class ItemContext(
        var requestCategoryId: String = "",
        var requestItemId: String = "",
        var requestItem: ItemModel = ItemModel.NONE,
        var responseItem: ItemModel = ItemModel.NONE,
        var responseCategory: CategoryModel = CategoryModel.NONE,
        var stubICreateCase: ItemCreateStubCases = ItemCreateStubCases.NONE,
        var stubIDeleteCase: ItemDeleteStubCases = ItemDeleteStubCases.NONE
): BaseContext()