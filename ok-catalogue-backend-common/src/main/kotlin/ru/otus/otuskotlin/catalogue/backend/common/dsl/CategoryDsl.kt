package ru.otus.otuskotlin.catalogue.backend.common.dsl

import ru.otus.otuskotlin.catalogue.backend.common.models.categories.CategoryModel

@CategoryDslMarker
class CategoryDsl {

    var id: String = ""
    var type: String = ""
    var name = CategoryNameDsl.EMPTY
        private set
    var parents = CategoryParentsDsl.EMPTY
        private set
    var children = CategoryChildrenDsl.EMPTY
        private set
    var items = CategoryItemsDsl.EMPTY
        private set
    var date = CategoryDateDsl.EMPTY
        private set


    fun name(conf: CategoryNameDsl.() -> Unit) {
        name = CategoryNameDsl().apply(conf)
    }

    fun parents(conf: CategoryParentsDsl.() -> Unit){
        parents = CategoryParentsDsl().apply(conf)
    }

    fun children(conf: CategoryChildrenDsl.() -> Unit){
        children = CategoryChildrenDsl().apply(conf)
    }

    fun date(conf: CategoryDateDsl.() -> Unit){
        date = CategoryDateDsl().apply(conf)
    }

    fun items(conf: CategoryItemsDsl.() -> Unit){
        items = CategoryItemsDsl().apply(conf)
    }
}

fun category(conf: CategoryDsl.() -> Unit) =
        CategoryDsl().apply(conf).run {
            CategoryModel(
                    id = id,
                    type = type,
                    label = name.label,
                    parentId = parents.parentId,
                    parents = parents.get(),
                    children = children.get(),
                    items = items.get(),
                    creationDate = date.creation,
                    modifyDate = date.modify
            )
        }
