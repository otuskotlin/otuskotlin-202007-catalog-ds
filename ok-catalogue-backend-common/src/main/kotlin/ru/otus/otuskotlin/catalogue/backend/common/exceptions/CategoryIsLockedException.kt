package ru.otus.otuskotlin.catalogue.backend.common.exceptions

class CategoryIsLockedException(id: String, label: String): Throwable(
        "Category $label with id=$id is locked now. Try again later."
)