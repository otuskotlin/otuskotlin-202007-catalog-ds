package ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories

import com.datastax.driver.extras.codecs.jdk8.LocalDateCodec
import com.datastax.driver.mapping.annotations.*
import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel
import ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories.CategoryCassandraDTO.Companion.CATEGORY_TABLE_NAME
import java.time.LocalDate
import java.util.*

@Table(name = CATEGORY_TABLE_NAME)
data class CategoryCassandraDTO (
    @PartitionKey(0)
    @Column(name = COLUMN_ID)
    val id: String? = null,

    @Column(name = COLUMN_TYPE)
    val type: String? = null,

    @Column(name = COLUMN_LABEL)
    val label: String? = null,

    @Column(name = COLUMN_PARENT_ID)
    val parentId: String? = null,


    @Column(name = COLUMN_CHILDREN_ID)
    val children: Set<CategoryHeaderCassandraDTO>? = null,

    @Column(name = COLUMN_ITEMS_ID)
    val items: Set<String>? = null,

    @Column(name = COLUMN_CREATION_DATE, codec = LocalDateCodec::class)
    val creationDate: LocalDate? = null,

    @Column(name = COLUMN_MODIFY_DATE, codec = LocalDateCodec::class)
    var modifyDate: LocalDate? = null,

    @Column(name = COLUMN_LOCK_VERSION)
    val lockVersion: String = UUID.randomUUID().toString()
) {
    fun toModel() = CategoryModel(
            id = id?:"",
            type = type?:"",
            label = label?:"",
            parentId = parentId?:"",
            children = children?.map { it.toModel() }?.toMutableSet()?: mutableSetOf(),
            creationDate = creationDate?: LocalDate.MIN,
            modifyDate = modifyDate?: creationDate?: LocalDate.MIN
    )

    companion object{
        const val CATEGORY_TABLE_NAME = "categories"
        const val COLUMN_ID = "id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_LABEL = "label"
        const val COLUMN_PARENT_ID = "parent_id"
        const val COLUMN_CHILDREN_ID = "children_id"
        const val COLUMN_ITEMS_ID = "items_id"
        const val COLUMN_CREATION_DATE = "creation_date"
        const val COLUMN_MODIFY_DATE = "modify_date"
        const val COLUMN_LOCK_VERSION= "lock_version"

        fun of(model: CategoryModel) = of(model, model.id)

        fun  of(model: CategoryModel, id: String) = CategoryCassandraDTO(
                id = id.takeIf { it.isNotBlank() },
                type = model.type.takeIf { it.isNotBlank() },
                label = model.label.takeIf { it.isNotBlank() },
                parentId = model.parentId.takeIf { it.isNotBlank() },
                children = model.children.map { CategoryHeaderCassandraDTO.of(it) }.toSet().takeIf { it.isNotEmpty() },
                items = model.items.map { it.id }.toSet().takeIf { it.isNotEmpty() },
                creationDate = model.creationDate.takeIf { it != LocalDate.MIN },
                modifyDate = model.modifyDate.takeIf { it != LocalDate.MIN }
        )
    }
}