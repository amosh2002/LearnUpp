package com.learnupp.data.messages

import com.learnupp.domain.model.MessageCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryMessagesStorage : MessagesStorage {
    private val categoriesState = MutableStateFlow<List<MessageCategory>>(emptyList())

    override fun getCategories(): Flow<List<MessageCategory>> = categoriesState.asStateFlow()

    override suspend fun save(categories: List<MessageCategory>) {
        categoriesState.value = categories
    }

    override suspend fun clear() {
        categoriesState.value = emptyList()
    }
}

