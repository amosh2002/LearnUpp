package com.mcp.ui.more

import com.mcp.domain.usecase.auth.LoginUseCase
import com.mcp.domain.usecase.auth.LoginUserParams
import com.mcp.domain.usecase.auth.LogoutUseCase
import com.mcp.ui.base.BaseScreenModel
import com.mcp.util.MCPStrings
import com.mcp.util.getValue
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


