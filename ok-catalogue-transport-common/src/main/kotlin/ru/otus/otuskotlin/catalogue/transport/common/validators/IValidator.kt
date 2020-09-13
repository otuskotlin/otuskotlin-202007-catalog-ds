package ru.otus.otuskotlin.catalogue.transport.common.validators

interface IValidator<T> {
    fun  validate(arg: T):ValidationResult
}