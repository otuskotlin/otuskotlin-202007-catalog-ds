package ru.otus.otuskotlin.catalogue.transport.common.validators

class HandleError(
        val code:String = "",
        val level:Level = Level.NONE,
        val message:String = ""
) {
    enum class Level{
        NONE,
        WARNING,
        ERROR
    }

}