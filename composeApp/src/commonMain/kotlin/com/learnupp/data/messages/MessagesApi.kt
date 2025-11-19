package com.learnupp.data.messages

import com.learnupp.domain.model.MessageCategory

interface MessagesApi {
    suspend fun fetchCategories(): List<MessageCategory>
}

