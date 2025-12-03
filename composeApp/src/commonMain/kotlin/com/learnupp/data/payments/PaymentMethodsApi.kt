package com.learnupp.data.payments

import com.learnupp.domain.model.payments.PaymentMethod

interface PaymentMethodsApi {
    suspend fun fetchMethods(): List<PaymentMethod>
}

