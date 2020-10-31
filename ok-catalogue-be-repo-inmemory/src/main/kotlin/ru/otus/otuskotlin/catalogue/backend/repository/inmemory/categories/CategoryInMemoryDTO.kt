package ru.otus.otuskotlin.catalogue.backend.repository.inmemory.categories

import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import java.time.LocalDate

data class CategoryInMemoryDTO (
    val id: String? = null,
    val type: String? = null,
    val label: String? = null,
    val parentId: String? = null,
    val creationDate: String? = null,
    var modifyDate: String? = null

) {
    fun toModel() = CategoryModel(
        id = id?:"",
        type = type?:"",
        label = label?:"",
        parentId = parentId?:"",
        creationDate = creationDate?.let { LocalDate.parse(it) }?: LocalDate.MIN,
        modifyDate = modifyDate?.let { LocalDate.parse(it) }?: creationDate?.let { LocalDate.parse(it) }?: LocalDate.MIN
    )


    companion object{

        fun of(model: CategoryModel) = of(model, model.id)

        fun  of(model: CategoryModel, id: String) = CategoryInMemoryDTO(
            id = id.takeIf { it.isNotBlank() },
            type = model.type.takeIf { it.isNotBlank() },
            label = model.label.takeIf { it.isNotBlank() },
            parentId = model.parentId.takeIf { it.isNotBlank() },
            creationDate = model.creationDate.takeIf { it != LocalDate.MIN }?.toString(),
            modifyDate = model.modifyDate.takeIf { it != LocalDate.MIN }?.toString()
        )
    }
}