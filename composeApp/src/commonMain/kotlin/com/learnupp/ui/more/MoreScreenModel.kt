package com.learnupp.ui.more

import com.learnupp.domain.usecase.auth.LogoutUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MoreScreenModel(
    private val logoutUseCase: LogoutUseCase
) : BaseScreenModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    suspend fun logout(): Boolean {
        _isLoading.value = true
        val success = logoutUseCase()

        // success == true if server returned 200 and SessionManager.logout() was called
        _isLoading.value = false
        return success
    }
}


