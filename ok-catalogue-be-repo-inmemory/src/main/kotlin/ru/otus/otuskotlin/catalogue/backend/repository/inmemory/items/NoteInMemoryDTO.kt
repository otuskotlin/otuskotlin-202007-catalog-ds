package ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items

import ru.otus.otuskotlin.catalogue.backend.common.models.items.NoteModel

data class NoteInMemoryDTO (
        val id: String? = null,
        val categoryId: String? = null,
        val header: String? = null,
        val description: String? = null,
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
        fun of(model: NoteModel) = NoteInMemoryDTO(
                id = model.id.takeIf { it.isNotBlank() },
                categoryId = model.categoryId.takeIf { it.isNotBlank() },
                header = model.header.takeIf { it.isNotBlank() },
                description = model.description.takeIf { it.isNotBlank() },
                preview = model.preview.takeIf { it.isNotBlank() }
        )
    }
}