package com.learnupp.domain.usecase.payments

import com.learnupp.domain.model.payments.PaymentMethod
import com.learnupp.domain.repo.PaymentMethodsRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetPaymentMethodsUseCase : ParameterlessUseCase<Flow<List<PaymentMethod>>>()

class GetPaymentMethodsUseCaseImpl(
    private val repository: PaymentMethodsRepository
) : GetPaymentMethodsUseCase() {
    override fun invoke(): Flow<List<PaymentMethod>> = repository.getMethods()
}

