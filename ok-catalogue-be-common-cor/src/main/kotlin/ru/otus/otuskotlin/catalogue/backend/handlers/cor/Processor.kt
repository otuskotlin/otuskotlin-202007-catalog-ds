package ru.otus.otuskotlin.catalogue.backend.handlers.cor

import kotlin.Exception

class Processor<T> private constructor(
    private val matcher: CorMatcher<T>,
    private val handlers: MutableList<IExec<T>>,
    private val onError: CorOnError<T>
): IExec<T>{

    @CorDslMarker
    class Builder<T>{
        private var matcher: CorMatcher<T> = {true}
        private var handlers: MutableList<IExec<T>> = mutableListOf()
        private var onError: CorOnError<T> = {throw it}

        fun isMatchable(block: CorMatcher<T>){
            matcher = block
        }

        fun exec(block: CorHandler<T>){
            handlers.add(
                corHandler {
                    exec(block)
                     //exec{block}
                }
            )
        }

        fun exec(block: IExec<T>) = handlers.add(block)

        fun handler(block: Handler.Builder<T>.() -> Unit) = handlers.add(corHandler(block))

        fun onError(block: CorOnError<T>){
            onError = block
        }

        fun processor(block: Processor.Builder<T>.() -> Unit) = handlers.add(corProc(block))

        fun build() = Processor<T>(
            matcher = matcher,
            handlers = handlers,
            onError = onError
        )
    }

    override suspend fun exec(ctx: T) {
        try {
            if(matcher(ctx))
                handlers.forEach {
                    it.exec(ctx)
                }
        }
        catch (e: Exception){
            onError(ctx, e)
        }
    }
}