package com.learnupp.domain.model.earnings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EarningsSummary(
    @SerialName("total_earned") val totalEarned: Double,
    @SerialName("this_month") val thisMonth: Double,
    @SerialName("last_month") val lastMonth: Double,
    @SerialName("transactions") val transactions: List<EarningsTransaction>
)

@Serializable
data class EarningsTransaction(
    val id: String,
    val title: String,
    val date: String,
    val amount: Double
)


