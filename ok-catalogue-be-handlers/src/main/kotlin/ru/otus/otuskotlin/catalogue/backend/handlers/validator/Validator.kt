package ru.otus.otuskotlin.catalogue.backend.handlers.validator

class Validator<T, R> private constructor(
    val validators: MutableList<validateHandler<T, R>> = mutableListOf(),
    private val errors: MutableList<R> = mutableListOf()
): IValidator<T, R> {

    class Builder<T, R> {
        fun build() = Validator<T, R>(
            validators = validators
        )

        private var validators: MutableList<validateHandler<T, R>> = mutableListOf()

        fun validate(block: validateHandler<T, R>){
            validators.add(block)
        }


    }

    override suspend fun validate(ctx: T): MutableList<R> {
        validators.forEach {
            it(ctx)?.let { it1 -> errors.add(it1) }
        }
        return errors
    }
}