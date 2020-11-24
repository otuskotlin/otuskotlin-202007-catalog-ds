package ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories

import com.datastax.driver.mapping.annotations.UDT
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel

@UDT(name = "child")
data class CategoryHeaderCassandraDTO(
        val id: String? = null,
        val label: String? = null,
        val type: String? = null
){
    fun toModel() = CategoryModel(
            id = id?:"",
            type = type?:"",
            label = label?:""
    )

    companion object{
        fun of(model: CategoryModel) = CategoryHeaderCassandraDTO(
                id = model.id.takeIf { it.isNotBlank() },
                type = model.type.takeIf { it.isNotBlank() },
                label = model.label.takeIf { it.isNotBlank() }
        )
    }
}