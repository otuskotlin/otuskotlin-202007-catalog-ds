package ru.otus.otuskotlin.catalogue.backend.common.exceptions

import ru.otus.otuskotlin.catalogue.backend.common.repositories.IItemRepository
import kotlin.reflect.KClass

class ItemRepoNotFoundException(id: String): Throwable("Item with ID=$id was not found")