package ru.otus.otuskotlin.catalogue.backend.common.exceptions

class CategoryRepoWrongIdException(id: String): Throwable("Incorrect ID = $id for category")