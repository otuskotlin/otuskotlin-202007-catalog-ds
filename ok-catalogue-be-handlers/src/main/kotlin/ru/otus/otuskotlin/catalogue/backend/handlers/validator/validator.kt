package ru.otus.otuskotlin.catalogue.backend.handlers.validator

typealias validateHandler<T, R> = suspend T.() -> R?

fun <T, R> validator(block: Validator.Builder<T, R>.() -> Unit): Validator<T, R> =
    Validator.Builder<T, R>().apply(block).build()