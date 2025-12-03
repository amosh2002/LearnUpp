package com.learnupp.data.settings.notifications

import com.learnupp.domain.model.settings.NotificationSetting
import kotlinx.coroutines.flow.StateFlow

interface NotificationsStorage {
    fun getSettings(): StateFlow<List<NotificationSetting>>
    suspend fun save(settings: List<NotificationSetting>)
}

