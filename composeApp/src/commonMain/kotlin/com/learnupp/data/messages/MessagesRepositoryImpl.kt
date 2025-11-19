package com.learnupp.data.messages

import com.learnupp.domain.repo.MessagesRepository
import kotlinx.coroutines.flow.firstOrNull

class MessagesRepositoryImpl(
    private val api: MessagesApi,
    private val storage: MessagesStorage
) : MessagesRepository {
    override fun getCategories() = storage.getCategories()

    override suspend fun refreshData() {
        val list = api.fetchCategories()
        storage.save(list)
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getCategories().firstOrNull().isNullOrEmpty()
    }
}

