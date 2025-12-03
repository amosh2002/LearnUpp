package com.learnupp.data.payments

import com.learnupp.domain.model.payments.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryPaymentMethodsStorage : PaymentMethodsStorage {
    private val state = MutableStateFlow<List<PaymentMethod>>(emptyList())

    override fun getMethods(): StateFlow<List<PaymentMethod>> = state.asStateFlow()

    override suspend fun save(methods: List<PaymentMethod>) {
        state.value = methods
    }
}

