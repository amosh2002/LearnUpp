package com.learnupp.domain.usecase.payments

import com.learnupp.domain.repo.PaymentMethodsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadPaymentMethodsUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadPaymentMethodsUseCaseImpl(
    private val repository: PaymentMethodsRepository
) : ReloadPaymentMethodsUseCase() {
    override suspend fun invoke() {
        repository.refreshData()
    }
}

