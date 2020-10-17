package ru.otus.otuskotlin.catalogue.backend.common.validators

interface IValidator<T, R> {
    suspend fun validate(ctx: T): MutableList<out R>
}