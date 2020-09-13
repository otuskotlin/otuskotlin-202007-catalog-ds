package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryInfoModel
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryType
import ru.otus.otuskotlin.catalogue.backend.common.models.FullCategoryModel
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.*
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.util.*

fun CategoryContext.setQuery(create: CategoryCreateQuery) = this.apply {
    reguestCategory = create.model()
}

fun CategoryContext.setQuery(delete: CategoryDeleteQuery) = this.apply {
    requestCategoryId = delete.categoryId?:""
}

fun CategoryContext.setQuery(get: CategoryGetQuery) = this.apply {
    requestCategoryId = get.categoryId?:""
}

fun CategoryContext.setQuery(rename: CategoryRenameQuery) = this.apply {
    requestCategoryId = rename.categoryId?:""
    requestLabel = rename.modLabel?:""
}

fun CategoryContext.setQuery(getMap: CategoryGetMapQuery) = this.apply {
    requestCategoryId = getMap.id?:""
}

fun CategoryContext.resultCategory() = CategoryGetResponse(
    data = responseCategory.toDTO(),
    status = status.toDTO()
)

fun CategoryContext.resultMap() = CategoryGetMapResponse(
        data = responseMap.toTreeDTO(),
        status = status.toDTO()
)

fun CategoryCreateQuery.model() = CategoryModel(
    id = UUID.randomUUID().toString(),
    label = label?:"",
    parentId = parentId?:"",
    creationDate = LocalDate.now(),
    //type = try{CategoryType.valueOf(type?:"".toLowerCase())} catch (e:IllegalArgumentException){CategoryType.NONE}
    type = CategoryType.NONE.findByArg((type?:"").toLowerCase())
)

fun FullCategoryModel.toDTO() = CategoryDTO(
        id = id.toDTOString(),
        type = type.toString().toDTOString(),
        label = label.toDTOString(),
        parentList = parents.map { it.toDTO() }.toMutableList(),
        childList = children.map { it.toDTO() }.toMutableList(),
        itemList = items.map { it.toDTO() }.toMutableList(),
        creationDate = creationDate.toString(),
        modifyDate = modifyDate.toString()
)

fun CategoryInfoModel.toDTO() = CategoryInfo(
        id = id.toDTOString(),
        label = label.toDTOString()
)

/**
 * Recursive fun for return catalogue tree
 */
fun CategoryInfoModel.toTreeDTO():CategoryMapDTO {
    if(tree.isEmpty())
        return  CategoryMapDTO(
            id = id.toDTOString(),
            label = label.toDTOString()
    )
    var branch: MutableList<CategoryMapDTO> = mutableListOf()
    for(node in tree){
        branch.add(node.toTreeDTO())
    }
    return CategoryMapDTO(
            id = id.toDTOString(),
            label = label.toDTOString(),
            children = branch
    )
}

fun CategoryContextStatus.toDTO() = CategoryError(
        level = CategoryError.Level.valueOf(this.toString())
)

internal fun String.toDTOString() = this.takeIf { it.isNotBlank() }