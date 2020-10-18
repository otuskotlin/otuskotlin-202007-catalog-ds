package ru.otus.otuskotlin.catalogue.backend.common.contexts

enum class ContextStatus {
    NONE,
    RUNNING,
    SUCCESS,
    FAILING,
    ERROR,
    FINISHING;

    val isError
        get() = this in arrayOf(ERROR, FAILING)
}