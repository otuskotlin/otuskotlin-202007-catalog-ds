package ru.otus.otuskotlin.catalogue.backend.common.models.items

data class NoteModel(
    override var id: String = "",
    override var categoryId: String = "",
    override var header: String = "",
    override var description: String = "",
    var preview: String = ""
):ItemModel(
    id = id,
    categoryId = categoryId,
    header = header,
    description = description
) {
}