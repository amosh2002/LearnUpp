package com.learnupp.domain.usecase.base

abstract class BaseUseCase<in Params, out Result> {
    abstract operator fun invoke(params: Params): Result
}

abstract class BaseSuspendUseCase<in Params, out Result> {
    abstract suspend operator fun invoke(params: Params): Result
}
