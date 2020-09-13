package ru.otus.otuskotlin.catalogue.transport.common.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class CategoryGetMapResponse(
    var data:CategoryMapDTO? = null,
    var status:CategoryError? = null
) {

}