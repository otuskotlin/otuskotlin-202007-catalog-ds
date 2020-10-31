package ru.otus.otuskotlin.catalogue.backend.repository.cassandra.items

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel

@Table(name = "ITEM_TABLE_NAME")
data class NoteCassandraDTO(
        @PartitionKey(0)
        @Column(name = "COLUMN_ID")
        val id: String? = null,

        @Column(name = "COLUMN_CATEGORY_ID")
        val categoryId: String? = null,

        @Column(name = "COLUMN_HEADER")
        val header: String? = null,

        @Column(name = "COLUMN_DESCRIPTION")
        val description: String? = null,

        @Column(name = "COLUMN_PREVIEW")
        val preview: String? = null
) {
    fun toModel() = NoteModel(
            id = id?:"",
            categoryId = categoryId?:"",
            header = header?:"",
            description = description?:"",
            preview = preview?:""
    )

    companion object {
        const val ITEM_TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_CATEGORY_ID = "category_id"
        const val COLUMN_HEADER = "header"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PREVIEW = "preview"

        fun of(model: NoteModel) = NoteCassandraDTO(
                id = model.id.takeIf { it.isNotBlank() },
                categoryId = model.categoryId.takeIf { it.isNotBlank() },
                header = model.header.takeIf { it.isNotBlank() },
                description = model.description.takeIf { it.isNotBlank() },
                preview = model.preview.takeIf { it.isNotBlank() }
        )
    }
}