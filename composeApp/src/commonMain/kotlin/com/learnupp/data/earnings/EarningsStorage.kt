package com.learnupp.data.earnings

import com.learnupp.domain.model.earnings.EarningsSummary
import kotlinx.coroutines.flow.StateFlow

interface EarningsStorage {
    fun getSummary(): StateFlow<EarningsSummary?>
    suspend fun save(summary: EarningsSummary)
}

