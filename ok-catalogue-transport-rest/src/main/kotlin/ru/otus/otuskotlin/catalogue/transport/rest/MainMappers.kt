package ru.otus.otuskotlin.catalogue.transport.rest

import ru.otus.otuskotlin.catalogue.backend.common.contexts.BaseContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.CategoryContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ContextStatus
import ru.otus.otuskotlin.catalogue.backend.common.contexts.ItemContext
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.*
import ru.otus.otuskotlin.catalogue.transport.common.models.ErrorDTO
import ru.otus.otuskotlin.catalogue.transport.common.models.ResponseModel
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.queries.*
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.responses.*
import ru.otus.otuskotlin.catalogue.transport.common.models.items.ItemResponse
import java.lang.ClassCastException
import java.time.LocalDate
import java.util.*

fun CategoryContext.setQuery(create: CategoryCreateQuery) = this.apply {
    requestCategory = create.model()
    stubCCreateCase = when(create.debug?.stub){
        CategoryCreateQuery.StubCases.SUCCESS -> CategoryCreateStubCases.SUCCESS
        else -> CategoryCreateStubCases.NONE
    }
}

fun CategoryContext.setQuery(delete: CategoryDeleteQuery) = this.apply {
    requestCategoryId = delete.categoryId?:""
    stubCDeleteCase = when(delete.debug?.stub){
        CategoryDeleteQuery.StubCases.SUCCESS -> CategoryDeleteStubCases.SUCCESS
        else -> CategoryDeleteStubCases.NONE
    }
}

fun CategoryContext.setQuery(get: CategoryGetQuery) = this.apply {
    requestCategoryId = get.categoryId?:""
    stubCGetCase = when(get.debug?.stub){
        CategoryGetQuery.StubCases.SUCCESS -> CategoryGetStubCases.SUCCESS
        else -> CategoryGetStubCases.NONE
    }
}

fun CategoryContext.setQuery(rename: CategoryRenameQuery) = this.apply {
    requestCategoryId = rename.categoryId?:""
    requestLabel = rename.modLabel?:""
    stubCRenameCase = when(rename.debug?.stub){
        CategoryRenameQuery.StubCases.SUCCESS -> CategoryRenameStubCases.SUCCESS
        else -> CategoryRenameStubCases.NONE
    }
}

fun CategoryContext.setQuery(getMap: CategoryGetMapQuery) = this.apply {
    requestCategoryId = getMap.id?:""
    stubCGetMapCase = when(getMap.debug?.stub){
        CategoryGetMapQuery.StubCases.SUCCESS -> CategoryGetMapStubCases.SUCCESS
        else -> CategoryGetMapStubCases.NONE
    }
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
fun CategoryModel.toTreeDTO(): CategoryMapDTO {
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

fun ContextStatus.toDTO() = ErrorDTO(
        level = ErrorDTO.Level.valueOf(this.toString())
)

internal fun String.toDTOString() = this.takeIf { it.isNotBlank() }

inline fun <reified T: ResponseModel> BaseContext.getResult():T{
    return when(T::class){
        CategoryGetResponse::class -> (this as CategoryContext).resultCategory() as T
        ItemResponse::class -> (this as ItemContext).resultItem() as T
        CategoryGetMapResponse::class -> (this as CategoryContext).resultMap() as T
        else -> throw ClassCastException("Invalid type")
    }
}