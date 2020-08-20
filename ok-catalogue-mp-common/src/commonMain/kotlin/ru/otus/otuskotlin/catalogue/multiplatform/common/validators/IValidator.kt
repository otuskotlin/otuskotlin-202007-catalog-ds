package ru.otus.otuskotlin.catalogue.multiplatform.common.validators

interface IValidator<T> {
    fun  validate(arg: T):ValidationResult
}