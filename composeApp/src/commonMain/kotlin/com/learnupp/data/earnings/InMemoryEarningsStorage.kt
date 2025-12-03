package com.learnupp.data.earnings

import com.learnupp.domain.model.earnings.EarningsSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryEarningsStorage : EarningsStorage {
    private val state = MutableStateFlow<EarningsSummary?>(null)

    override fun getSummary(): StateFlow<EarningsSummary?> = state.asStateFlow()

    override suspend fun save(summary: EarningsSummary) {
        state.value = summary
    }
}

