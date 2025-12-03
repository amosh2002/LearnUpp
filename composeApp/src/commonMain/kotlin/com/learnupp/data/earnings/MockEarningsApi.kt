package com.learnupp.data.earnings

import com.learnupp.domain.model.earnings.EarningsSummary
import com.learnupp.domain.model.earnings.EarningsTransaction
import kotlin.random.Random

class MockEarningsApi : EarningsApi {
    override suspend fun fetchSummary(): EarningsSummary {
        val transactions = (0 until 5).map { index ->
            EarningsTransaction(
                id = "tx-$index",
                title = "Course Purchase: Sample ${index + 1}",
                date = "Nov ${24 - index}, 2025",
                amount = 25 + Random(index).nextDouble(10.0, 30.0)
            )
        }
        return EarningsSummary(
            totalEarned = 1245.32,
            thisMonth = 243.10,
            lastMonth = 189.45,
            transactions = transactions
        )
    }
}

