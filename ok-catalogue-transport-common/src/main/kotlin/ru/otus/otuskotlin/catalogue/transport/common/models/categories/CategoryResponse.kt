package ru.otus.otuskotlin.catalogue.transport.common.models.categories

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryError
import ru.otus.otuskotlin.catalogue.transport.common.models.categories.CategoryInfo

/**
 *  Deprecated
 *  To think about to use it
 */
@Serializable
abstract class CategoryResponse(
        @Transient open var parentList:MutableList<CategoryInfo>? = null,
        @Transient open var type:String? = null,
        @Transient open var label:String? = null,
        @Transient open var status: CategoryError? = null
) {
}