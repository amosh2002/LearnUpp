package com.learnupp.data.settings.notifications

import com.learnupp.domain.model.settings.NotificationGroup
import com.learnupp.domain.model.settings.NotificationSetting

class MockNotificationsApi : NotificationsApi {
    private var current = initialSettings()

    override suspend fun fetchSettings(): List<NotificationSetting> = current

    override suspend fun updateSetting(id: String, enabled: Boolean): List<NotificationSetting> {
        current = current.map {
            if (it.id == id) it.copy(enabled = enabled) else it
        }
        return current
    }

    private fun initialSettings(): List<NotificationSetting> = listOf(
        NotificationSetting("push", "Push Notifications", null, true, NotificationGroup.GENERAL),
        NotificationSetting("email", "Email Notifications", null, true, NotificationGroup.GENERAL),
        NotificationSetting("sms", "SMS Notifications", null, false, NotificationGroup.GENERAL),
        NotificationSetting("course_updates", "Course Updates", null, true, NotificationGroup.APP_ACTIVITY),
        NotificationSetting("comments", "Comments & Mentions", null, true, NotificationGroup.APP_ACTIVITY),
        NotificationSetting("messages", "Messages", null, true, NotificationGroup.APP_ACTIVITY),
        NotificationSetting(
            "mute_all",
            "Mute All Notifications",
            "Temporarily pause all alerts",
            false,
            NotificationGroup.PREFERENCES
        )
    )
}

