package ru.otus.otuskotlin.catalogue.backend.repository.inmemory.items

abstract class ItemInMemoryDTO (
    open val id: String? = null,
    open val categoryId: String? = null,
    open val header: String? = null,
    open val description: String? = null
)
