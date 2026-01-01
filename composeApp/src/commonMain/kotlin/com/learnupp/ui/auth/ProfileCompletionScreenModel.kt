package com.learnupp.ui.auth

import com.learnupp.domain.usecase.auth.CheckUsernameUseCase
import com.learnupp.domain.usecase.auth.CompleteProfileUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileCompletionScreenModel(
    private val checkUsernameUseCase: CheckUsernameUseCase,
    private val completeProfileUseCase: CompleteProfileUseCase
) : BaseScreenModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    suspend fun checkUsername(username: String): Boolean =
        checkUsernameUseCase(username)

    suspend fun completeProfile(username: String, fullName: String?): Boolean {
        _isLoading.value = true
        return try {
            completeProfileUseCase(username, fullName)
        } finally {
            _isLoading.value = false
        }
    }
}

