package com.learnupp.data.settings.notifications

import com.learnupp.domain.model.settings.NotificationSetting

interface NotificationsApi {
    suspend fun fetchSettings(): List<NotificationSetting>
    suspend fun updateSetting(id: String, enabled: Boolean): List<NotificationSetting>
}

