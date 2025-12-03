package com.learnupp.domain.repo

import com.learnupp.domain.model.earnings.EarningsSummary
import kotlinx.coroutines.flow.Flow

interface EarningsRepository : BaseRepository {
    fun getSummary(): Flow<EarningsSummary?>
}

