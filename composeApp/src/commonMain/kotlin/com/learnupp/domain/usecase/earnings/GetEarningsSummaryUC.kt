package com.learnupp.domain.usecase.earnings

import com.learnupp.domain.model.earnings.EarningsSummary
import com.learnupp.domain.repo.EarningsRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetEarningsSummaryUseCase : ParameterlessUseCase<Flow<EarningsSummary?>>()

class GetEarningsSummaryUseCaseImpl(
    private val repository: EarningsRepository
) : GetEarningsSummaryUseCase() {
    override fun invoke(): Flow<EarningsSummary?> = repository.getSummary()
}

