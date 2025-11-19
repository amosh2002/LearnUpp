package com.learnupp.domain.usecase.messages

import com.learnupp.domain.repo.MessagesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadMessagesUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadMessagesUseCaseImpl(
    private val repository: MessagesRepository
) : PreloadMessagesUseCase() {
    override suspend fun invoke() {
        repository.preloadData()
    }
}

