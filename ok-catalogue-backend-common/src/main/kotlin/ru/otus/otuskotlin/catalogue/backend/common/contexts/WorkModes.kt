package ru.otus.otuskotlin.catalogue.backend.common.contexts

enum class WorkModes {
    TEST,
    PROD;

    companion object{
        val DEFAULT = PROD
    }
}