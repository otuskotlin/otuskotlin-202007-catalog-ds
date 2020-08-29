package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryType
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryCreateQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryGetQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryRenameQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.util.*

internal fun CategoryContext.setQuery(create: CategoryCreateQuery) = this.apply {
    reguestCategory = create.model()
}

internal fun CategoryContext.setQuery(delete: CategoryDeleteQuery) = this.apply {
    requestCategoryId = delete.categoryId?:""
}

internal fun CategoryContext.setQuery(get: CategoryGetQuery) = this.apply {
    requestCategoryId = get.categoryId?:""
}

internal fun CategoryContext.setQuery(rename: CategoryRenameQuery) = this.apply {
    requestCategoryId = rename.categoryId?:""
    requestLabel = rename.modLabel?:""
}


internal fun CategoryCreateQuery.model() = CategoryModel(
    id = UUID.randomUUID().toString(),
    label = label?:"",
    parentId = parentId?:"",
    creationDate = LocalDate.now(),
    //type = try{CategoryType.valueOf(type?:"".toLowerCase())} catch (e:IllegalArgumentException){CategoryType.NONE}
    type = CategoryType.NONE.findByArg((type?:"").toLowerCase())
)