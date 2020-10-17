package ru.otus.otuskotlin.catalogue.backend.handlers.cor

typealias CorMatcher<T> = suspend T.() -> Boolean
typealias CorHandler<T> = suspend T.() -> Unit
typealias CorOnError<T> = suspend T.(Throwable) -> Unit

fun <T> corHandler(block: Handler.Builder<T>.() -> Unit): Handler<T> = Handler.Builder<T>().apply(block).build()

fun <T> corProc(block: Processor.Builder<T>.() -> Unit): Processor<T> = Processor.Builder<T>().apply(block).build()