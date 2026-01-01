package com.learnupp.ui.auth

import com.learnupp.data.auth.OtpRequestResult
import com.learnupp.domain.usecase.auth.RequestOtpUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthStartScreenModel(
    private val requestOtpUseCase: RequestOtpUseCase
) : BaseScreenModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    suspend fun requestOtp(email: String): OtpRequestResult {
        _isLoading.value = true
        return try {
            requestOtpUseCase(email)
        } finally {
            _isLoading.value = false
        }
    }
}

