package ru.otus.otuskotlin.catalogue.backend.common.contexts

import ru.otus.otuskotlin.catalogue.backend.common.models.IErrorModel
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemCreateStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemDeleteStubCases
import ru.otus.otuskotlin.catalogue.backend.common.models.items.ItemModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository

data class ItemContext(
        override var workMode: WorkModes = WorkModes.DEFAULT,
        override var categoryRepo: ICategoryRepository = ICategoryRepository.NONE,
        override var categoryRepoTest: ICategoryRepository = ICategoryRepository.NONE,
        override var categoryRepoProd: ICategoryRepository = ICategoryRepository.NONE,
        override var errors: MutableList<IErrorModel> = mutableListOf(),
        override var status: ContextStatus = ContextStatus.NONE,
        var requestCategoryId: String = "",
        var requestItemId: String = "",
        var requestItem: ItemModel = ItemModel.NONE,
        var responseItem: ItemModel = ItemModel.NONE,
        var responseCategory: CategoryModel = CategoryModel.NONE,
        var stubICreateCase: ItemCreateStubCases = ItemCreateStubCases.NONE,
        var stubIDeleteCase: ItemDeleteStubCases = ItemDeleteStubCases.NONE
): BaseContext(
        workMode = workMode,
        categoryRepo = categoryRepo,
        categoryRepoTest = categoryRepoTest,
        categoryRepoProd = categoryRepoProd,
        errors = errors,
        status = status
)