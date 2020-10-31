package ru.otus.otuskotlin.catalogue.backend.common.exceptions

class ItemRepoWromgIdException(id: String): Throwable("Incorrect ID = $id for item")