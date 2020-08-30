package ru.otus.otuskotlin.catalogue.backend.common.models

/**
 *  Types of categories
 *  Use to define table for items in database and communicate with external services
 */
enum class CategoryType(val type: String) {
    NONE(""),
    NOTES("notes");

    fun findByArg(arg:String):CategoryType{
        for(node in values())
            if(arg == node.type) return node
        return NONE
    }
}