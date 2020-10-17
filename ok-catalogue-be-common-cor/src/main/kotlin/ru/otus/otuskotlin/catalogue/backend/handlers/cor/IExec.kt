package ru.otus.otuskotlin.catalogue.backend.handlers.cor

interface IExec<T> {
    suspend fun exec(ctx: T)
}