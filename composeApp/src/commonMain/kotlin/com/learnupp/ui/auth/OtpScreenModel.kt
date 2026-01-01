package com.learnupp.ui.auth

import com.learnupp.data.auth.OtpRequestResult
import com.learnupp.data.auth.VerifyOtpResult
import com.learnupp.domain.usecase.auth.RequestOtpUseCase
import com.learnupp.domain.usecase.auth.VerifyOtpUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OtpScreenModel(
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val requestOtpUseCase: RequestOtpUseCase
) : BaseScreenModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    suspend fun verify(email: String, code: String): VerifyOtpResult? {
        _isLoading.value = true
        return try {
            verifyOtpUseCase(email, code)
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun resend(email: String): OtpRequestResult {
        _isLoading.value = true
        return try {
            requestOtpUseCase(email)
        } finally {
            _isLoading.value = false
        }
    }
}

