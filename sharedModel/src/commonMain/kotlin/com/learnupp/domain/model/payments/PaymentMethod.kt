package com.learnupp.domain.model.payments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentMethodType {
    CARD, PAYPAL
}

@Serializable
data class PaymentMethod(
    val id: String,
    val label: String,
    val subtitle: String,
    @SerialName("masked_number") val maskedNumber: String,
    val type: PaymentMethodType,
    val active: Boolean = false,
    val primary: Boolean = false
)


