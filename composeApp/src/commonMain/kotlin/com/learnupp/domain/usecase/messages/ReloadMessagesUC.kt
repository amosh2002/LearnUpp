package com.learnupp.domain.usecase.messages

import com.learnupp.domain.repo.MessagesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadMessagesUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadMessagesUseCaseImpl(
    private val repository: MessagesRepository
) : ReloadMessagesUseCase() {
    override suspend fun invoke() {
        repository.refreshData()
    }
}

