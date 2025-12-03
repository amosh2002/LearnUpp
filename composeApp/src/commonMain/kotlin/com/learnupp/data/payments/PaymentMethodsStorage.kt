package com.learnupp.data.payments

import com.learnupp.domain.model.payments.PaymentMethod
import kotlinx.coroutines.flow.StateFlow

interface PaymentMethodsStorage {
    fun getMethods(): StateFlow<List<PaymentMethod>>
    suspend fun save(methods: List<PaymentMethod>)
}

