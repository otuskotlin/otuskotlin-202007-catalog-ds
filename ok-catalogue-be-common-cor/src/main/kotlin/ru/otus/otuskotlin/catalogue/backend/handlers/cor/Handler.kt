package ru.otus.otuskotlin.catalogue.backend.handlers.cor

class Handler<T> private constructor(
    private val matcher: CorMatcher<T> = {true},
    private val handler: CorHandler<T> = {},
    private val onError: CorOnError<T> = {throw it}
): IExec<T> {

    @CorDslMarker
    class Builder<T>{
        private var matcher: CorMatcher<T> = {true}
        private var handler: CorHandler<T> = {}
        private var onError: CorOnError<T> = {throw it}

        fun isMatchable(block: CorMatcher<T>){
            matcher = block
        }

        fun exec(block: CorHandler<T>){
            handler = block
        }

        fun onError(block: CorOnError<T>){
            onError = block
        }

        fun build() = Handler<T>(
            matcher = matcher,
            handler = handler,
            onError = onError
        )
    }

    override suspend fun exec(ctx: T) {
        try {
           if(matcher(ctx))
               handler(ctx)
        }
        catch (e: Exception){
           onError(ctx, e)
        }
    }
}