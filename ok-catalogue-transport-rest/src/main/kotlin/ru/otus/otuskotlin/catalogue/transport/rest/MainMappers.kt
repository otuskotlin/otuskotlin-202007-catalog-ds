package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.CategoryContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryInfoModel
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.common.models.CategoryType
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.*
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemDeleteQuery
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemInfo
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemResponse
import java.lang.ClassCastException
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.util.*

fun CategoryContext.setQuery(create: CategoryCreateQuery) = this.apply {
    requestCategory = create.model()
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
        data = responseCategory.toTreeDTO(),
        status = status.toDTO()
)

fun CategoryCreateQuery.model() = CategoryModel(
    id = UUID.randomUUID().toString(),
    label = label?:"",
    parentId = parentId?:"",
    creationDate = LocalDate.now(),
    //type = try{CategoryType.valueOf(type?:"".toLowerCase())} catch (e:IllegalArgumentException){CategoryType.NONE}
    //type = CategoryType.NONE.findByArg((type?:"").toLowerCase())
    type = type?:"".toLowerCase()
)

fun CategoryModel.toDTO() = CategoryDTO(
        id = id.toDTOString(),
        type = type.toDTOString(),
        label = label.toDTOString(),
        parentList = parents.map { it.toInfoDTO() }.toMutableList(),
        childList = children.map { it.toInfoDTO() }.toMutableList(),
        itemList = items.map { it.toDTO() }.toMutableList(),
        creationDate = creationDate.toString(),
        modifyDate = modifyDate.toString()
)

fun CategoryModel.toInfoDTO() = CategoryInfo(
        id = id.toDTOString(),
        label = label.toDTOString()
)

//fun CategoryInfoModel.toDTO() = CategoryInfo(
//        id = id.toDTOString(),
//        label = label.toDTOString()
//)

/**
 * Recursive fun for return catalogue tree
 */
fun CategoryModel.toTreeDTO():CategoryMapDTO {
    if(children.isEmpty())
        return  CategoryMapDTO(
                id = id.toDTOString(),
                label = label.toDTOString()
        )
    val branch: MutableList<CategoryMapDTO> = mutableListOf()
    for(node in children){
        branch.add(node.toTreeDTO())
    }
    return CategoryMapDTO(
            id = id.toDTOString(),
            label = label.toDTOString(),
            children = branch
    )
}

//fun CategoryInfoModel.toTreeDTO():CategoryMapDTO {
//    if(tree.isEmpty())
//        return  CategoryMapDTO(
//            id = id.toDTOString(),
//            label = label.toDTOString()
//    )
//    val branch: MutableList<CategoryMapDTO> = mutableListOf()
//    for(node in tree){
//        branch.add(node.toTreeDTO())
//    }
//    return CategoryMapDTO(
//            id = id.toDTOString(),
//            label = label.toDTOString(),
//            children = branch
//    )
//}

fun CategoryContextStatus.toDTO() = CategoryError(
        level = CategoryError.Level.valueOf(this.toString())
)

internal fun String.toDTOString() = this.takeIf { it.isNotBlank() }

inline fun <reified T> CategoryContext.getResult():T{
    return when(T::class){
        CategoryGetResponse::class -> resultCategory() as T
        ItemResponse::class -> resultItem() as T
        CategoryGetMapResponse::class -> resultMap() as T
        else -> throw ClassCastException("Invalid type")
    }
}