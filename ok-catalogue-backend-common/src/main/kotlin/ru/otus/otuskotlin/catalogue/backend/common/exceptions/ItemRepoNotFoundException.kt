package ru.otus.otuskotlin.catalogue.backend.common.exceptions

class ItemRepoNotFoundException(id: String): Throwable("Item with ID=$id was not found")