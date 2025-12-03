package com.learnupp.data.earnings

import com.learnupp.domain.model.earnings.EarningsSummary

interface EarningsApi {
    suspend fun fetchSummary(): EarningsSummary
}

