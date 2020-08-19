package ru.otus.otuskotlin.catalogue.multiplatform.transport.validators

interface IValidator<T> {
    fun  validate(arg: T):ValidationResult
}