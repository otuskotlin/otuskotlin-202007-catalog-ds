package ru.otus.otuskotlin.catalogue.backend.common.validators

typealias validateHandler<T, R> = suspend T.() -> R?

fun <T, R> validator(block: ValidatorProc.Builder<T, R>.() -> Unit): ValidatorProc<T, R> =
    ValidatorProc.Builder<T, R>().apply(block).build()