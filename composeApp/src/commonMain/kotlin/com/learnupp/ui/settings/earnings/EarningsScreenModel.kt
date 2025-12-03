package com.learnupp.ui.settings.earnings

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.model.earnings.EarningsSummary
import com.learnupp.domain.usecase.earnings.GetEarningsSummaryUseCase
import com.learnupp.domain.usecase.earnings.ReloadEarningsUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EarningsScreenModel(
    private val getSummary: GetEarningsSummaryUseCase,
    private val reloadSummary: ReloadEarningsUseCase
) : BaseScreenModel() {

    val summary = getSummary()
        .stateIn(screenModelScope, SharingStarted.Eagerly, null)

    init {
        screenModelScope.launch { reloadSummary() }
    }
}

