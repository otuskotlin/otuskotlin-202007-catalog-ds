package ru.otus.otuskotlin.catalogue.backend.logics.handlers

import ru.otus.otuskotlin.catalogue.backend.common.contexts.BaseContext
import ru.otus.otuskotlin.catalogue.backend.common.contexts.WorkModes
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corHandler

inline fun <reified T: BaseContext> setRepoByWorkMode() = corHandler<T> {
    exec {
        categoryRepo = when(workMode){
            WorkModes.PROD -> categoryRepoProd
            WorkModes.TEST -> categoryRepoTest
        }
    }
}