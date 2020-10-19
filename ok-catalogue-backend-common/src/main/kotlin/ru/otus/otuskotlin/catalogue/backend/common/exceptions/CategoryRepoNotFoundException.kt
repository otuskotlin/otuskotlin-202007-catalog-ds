package ru.otus.otuskotlin.catalogue.backend.common.exceptions

class CategoryRepoNotFoundException(id: String): Throwable("Category with ID=$id was not found")