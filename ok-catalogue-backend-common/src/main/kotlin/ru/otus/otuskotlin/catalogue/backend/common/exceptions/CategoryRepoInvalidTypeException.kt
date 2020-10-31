package ru.otus.otuskotlin.catalogue.backend.common.exceptions

class CategoryRepoInvalidTypeException(type: String) : Throwable("Repository aliased with type: $type was not found.")