package ru.otus.otuskotlin.catalogue.backend.handlers.validator

infix fun <E> MutableCollection<E>.addRange(x: MutableCollection<E>): Boolean{
    return this.addAll(x)
}