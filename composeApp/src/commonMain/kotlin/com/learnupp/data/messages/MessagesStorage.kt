package com.learnupp.data.messages

import com.learnupp.domain.model.MessageCategory
import kotlinx.coroutines.flow.Flow

interface MessagesStorage {
    fun getCategories(): Flow<List<MessageCategory>>
    suspend fun save(categories: List<MessageCategory>)
    suspend fun clear()
}

