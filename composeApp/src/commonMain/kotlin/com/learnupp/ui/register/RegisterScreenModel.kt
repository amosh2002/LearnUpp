package com.learnupp.ui.register

import com.learnupp.domain.usecase.auth.RegisterUseCase
import com.learnupp.domain.usecase.auth.RegisterUserParams
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterScreenModel(
    private val registerUseCase: RegisterUseCase
) : BaseScreenModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        acceptedTerms: Boolean
    ): Boolean {
        _isLoading.value = true
        val success = registerUseCase(
            RegisterUserParams(
                fullName = fullName,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                acceptedTerms = acceptedTerms
            )
        )
        _isLoading.value = false
        return success
    }
}


