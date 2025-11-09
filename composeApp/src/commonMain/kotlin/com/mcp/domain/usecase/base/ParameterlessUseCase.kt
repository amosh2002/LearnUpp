package com.mcp.domain.usecase.base

abstract class ParameterlessUseCase<out Result> : BaseUseCase<Nothing, Result>() {
    abstract operator fun invoke(): Result

    final override fun invoke(params: Nothing): Result {
        return invoke()
    }
}

abstract class ParameterlessSuspendUseCase<out Result> : BaseSuspendUseCase<Nothing, Result>() {
    abstract suspend operator fun invoke(): Result

    final override suspend fun invoke(params: Nothing): Result {
        return invoke()
    }
}
