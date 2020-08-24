package ru.otus.otuskotlin.catalogue.backend.common.models

import java.awt.Image

abstract class ItemModel(
    open var id: String = "",
    open var header: String = "",
    open var description: String = ""
) {
}