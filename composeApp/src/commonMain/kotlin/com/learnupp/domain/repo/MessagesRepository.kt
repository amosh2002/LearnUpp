package com.learnupp.domain.repo

import com.learnupp.domain.model.MessageCategory
import kotlinx.coroutines.flow.Flow

interface MessagesRepository : BaseRepository {
    fun getCategories(): Flow<List<MessageCategory>>
}

