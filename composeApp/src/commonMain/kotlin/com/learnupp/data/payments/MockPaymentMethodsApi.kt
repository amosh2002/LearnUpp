package com.learnupp.data.payments

import com.learnupp.domain.model.payments.PaymentMethod
import com.learnupp.domain.model.payments.PaymentMethodType

class MockPaymentMethodsApi : PaymentMethodsApi {
    override suspend fun fetchMethods(): List<PaymentMethod> = listOf(
        PaymentMethod(
            id = "visa",
            label = "Visa Card",
            subtitle = "Personal",
            maskedNumber = "•••• 4821",
            type = PaymentMethodType.CARD,
            active = true,
            primary = true
        ),
        PaymentMethod(
            id = "mastercard",
            label = "Mastercard •••• 9012",
            subtitle = "Expires 12/25",
            maskedNumber = "•••• 9012",
            type = PaymentMethodType.CARD
        ),
        PaymentMethod(
            id = "paypal",
            label = "john.doe@example.com",
            subtitle = "PayPal Account",
            maskedNumber = "",
            type = PaymentMethodType.PAYPAL
        )
    )
}

