package com.learnupp.domain.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSetting(
    val id: String,
    val title: String,
    val description: String? = null,
    val enabled: Boolean,
    val group: NotificationGroup
)

@Serializable
enum class NotificationGroup {
    GENERAL,
    APP_ACTIVITY,
    PREFERENCES
}

@Serializable
data class NotificationSettingsPayload(
    val settings: List<NotificationSetting>
)


