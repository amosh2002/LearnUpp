package com.learnupp.data.settings.notifications

import com.learnupp.domain.model.settings.NotificationSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryNotificationsStorage : NotificationsStorage {
    private val state = MutableStateFlow<List<NotificationSetting>>(emptyList())

    override fun getSettings(): StateFlow<List<NotificationSetting>> = state.asStateFlow()

    override suspend fun save(settings: List<NotificationSetting>) {
        state.value = settings
    }
}

