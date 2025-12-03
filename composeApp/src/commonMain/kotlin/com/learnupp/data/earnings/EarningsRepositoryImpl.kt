package com.learnupp.data.earnings

import com.learnupp.domain.repo.EarningsRepository

class EarningsRepositoryImpl(
    private val api: EarningsApi,
    private val storage: EarningsStorage
) : EarningsRepository {
    override fun getSummary() = storage.getSummary()

    override suspend fun refreshData() {
        val summary = api.fetchSummary()
        storage.save(summary)
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getSummary().value == null
    }
}

