package com.learnupp.ui.login

import com.learnupp.domain.usecase.auth.LoginUseCase
import com.learnupp.domain.usecase.auth.LoginUserParams
import com.learnupp.ui.base.BaseScreenModel
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginScreenModel(
    private val loginUseCase: LoginUseCase
) : BaseScreenModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    suspend fun login(
        email: String,
        password: String
    ): String? {
        _isLoading.value = true

        val success =
            loginUseCase(
                LoginUserParams(
                    email,
                    password
                )
            )

        _isLoading.value = false

        return if (success) null else LearnUppStrings.LOGIN_ERROR_INVALID_CREDENTIALS.getValue()
    }
}


