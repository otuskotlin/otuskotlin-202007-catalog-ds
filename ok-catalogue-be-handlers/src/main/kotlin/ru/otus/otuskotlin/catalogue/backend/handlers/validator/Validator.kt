package ru.otus.otuskotlin.catalogue.backend.handlers.validator

class Validator<T, R> private constructor(
    val validators: MutableList<validateHandler<T, R>> = mutableListOf(),
    private val errors: MutableList<R> = mutableListOf(),
    private val stopOnError: Boolean = false
): IValidator<T, R> {

    class Builder<T, R> {
        private var validators: MutableList<validateHandler<T, R>> = mutableListOf()
        var stopOnError: Boolean = false

        fun build() = Validator<T, R>(
            validators = validators,
            stopOnError = stopOnError
        )


        fun validate(block: validateHandler<T, R>){
            validators.add(block)
        }


    }

    override suspend fun validate(ctx: T): MutableList<R> {
        validators.forEach {
            it(ctx)?.let {
                    it1 -> errors.add(it1)
                    if (stopOnError) return errors
            }
        }
        return errors
    }
}