package com.learnupp.domain.repo

import com.learnupp.domain.model.settings.NotificationSetting
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository : BaseRepository {
    fun getSettings(): Flow<List<NotificationSetting>>
    suspend fun toggleSetting(id: String, enabled: Boolean)
}

