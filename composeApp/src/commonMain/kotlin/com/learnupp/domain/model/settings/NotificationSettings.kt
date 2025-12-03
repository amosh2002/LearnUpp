package com.learnupp.domain.model.settings

import com.learnupp.domain.model.base.CommonSerializable
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSetting(
    val id: String,
    val title: String,
    val description: String? = null,
    val enabled: Boolean,
    val group: NotificationGroup
) : CommonSerializable

@Serializable
enum class NotificationGroup : CommonSerializable {
    GENERAL,
    APP_ACTIVITY,
    PREFERENCES
}

@Serializable
data class NotificationSettingsPayload(
    val settings: List<NotificationSetting>
) : CommonSerializable

