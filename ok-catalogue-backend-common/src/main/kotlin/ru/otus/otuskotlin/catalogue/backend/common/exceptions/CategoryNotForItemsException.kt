package ru.otus.otuskotlin.catalogue.backend.common.exceptions

class CategoryNotForItemsException(id: String): Throwable("Category id:$id is not allowed to contain items")