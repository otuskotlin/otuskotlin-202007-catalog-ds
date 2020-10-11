package ru.otus.otuskotlin.catalogue.backend.handlers.validator

interface IValidator<T, R> {
    suspend fun validate(ctx: T): MutableList<out R>
}