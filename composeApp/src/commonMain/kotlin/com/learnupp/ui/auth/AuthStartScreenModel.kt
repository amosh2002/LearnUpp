package com.learnupp.ui.auth

import com.learnupp.data.auth.OtpRequestResult
import com.learnupp.domain.model.AuthProvider
import com.learnupp.domain.usecase.auth.LoginWithProviderUseCase
import com.learnupp.domain.usecase.auth.RequestOtpUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthStartScreenModel(
    private val requestOtpUseCase: RequestOtpUseCase,
    private val loginWithProviderUseCase: LoginWithProviderUseCase
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

    suspend fun loginWithProvider(provider: AuthProvider) {
        _isLoading.value = true
        try {
            loginWithProviderUseCase(provider)
        } catch (e: Exception) {
            // Handle error, maybe expose error state
        } finally {
            _isLoading.value = false
        }
    }
}

