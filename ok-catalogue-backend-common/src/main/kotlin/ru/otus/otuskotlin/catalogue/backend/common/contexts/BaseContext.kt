package ru.otus.otuskotlin.catalogue.backend.common.contexts

import ru.otus.otuskotlin.catalogue.backend.common.models.IErrorModel
import ru.otus.otuskotlin.catalogue.backend.common.repositories.ICategoryRepository

abstract class BaseContext (
        open var workMode: WorkModes = WorkModes.DEFAULT,
        open var categoryRepo: ICategoryRepository = ICategoryRepository.NONE,
        open var categoryRepoTest: ICategoryRepository = ICategoryRepository.NONE,
        open var categoryRepoProd: ICategoryRepository = ICategoryRepository.NONE,
        open var errors: MutableList<IErrorModel> = mutableListOf(),
        open var status: ContextStatus = ContextStatus.NONE
)

