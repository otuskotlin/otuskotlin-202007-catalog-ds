package ru.otus.otuskotlin.catalogue.backend.common.models

enum class CategoryType(val type: String) {
    NONE(""),
    NOTES("notes");

    fun findByArg(arg:String):CategoryType{
        for(node in values())
            if(arg == node.type) return node
        return NONE
    }
}