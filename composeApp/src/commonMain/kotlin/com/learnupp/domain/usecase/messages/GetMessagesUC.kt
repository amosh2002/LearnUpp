package com.learnupp.domain.usecase.messages

import com.learnupp.domain.model.MessageCategory
import com.learnupp.domain.repo.MessagesRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetMessagesUseCase : ParameterlessUseCase<Flow<List<MessageCategory>>>()

class GetMessagesUseCaseImpl(
    private val repository: MessagesRepository
) : GetMessagesUseCase() {
    override fun invoke(): Flow<List<MessageCategory>> = repository.getCategories()
}

