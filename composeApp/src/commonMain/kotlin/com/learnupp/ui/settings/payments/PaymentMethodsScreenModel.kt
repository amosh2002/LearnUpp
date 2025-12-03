package com.learnupp.ui.settings.payments

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.usecase.payments.GetPaymentMethodsUseCase
import com.learnupp.domain.usecase.payments.ReloadPaymentMethodsUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PaymentMethodsScreenModel(
    private val getMethods: GetPaymentMethodsUseCase,
    private val reloadMethods: ReloadPaymentMethodsUseCase
) : BaseScreenModel() {

    val methods = getMethods()
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptyList())

    init {
        screenModelScope.launch { reloadMethods() }
    }
}

