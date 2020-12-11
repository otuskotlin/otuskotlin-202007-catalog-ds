package ru.otus.otuskotlin.catalogue.backend.common.exceptions

class ItemRepoWrongIdException(id: String): Throwable("Incorrect ID = $id for item")