package ru.otus.otuskotlin.catalogue.backend.common.exceptions

import ru.otus.otuskotlin.catalogue.backend.common.repositories.IItemRepository
import kotlin.reflect.KClass

class RepoClassNotFoundException(className: KClass<out IItemRepository>): Throwable("There was not found repository for class: $className")
