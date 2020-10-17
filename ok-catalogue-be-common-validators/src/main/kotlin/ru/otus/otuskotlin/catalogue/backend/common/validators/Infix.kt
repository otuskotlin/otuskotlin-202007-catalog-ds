package ru.otus.otuskotlin.catalogue.backend.common.validators

infix fun <E> MutableCollection<E>.addRange(x: MutableCollection<E>): Boolean{
    return this.addAll(x)
}