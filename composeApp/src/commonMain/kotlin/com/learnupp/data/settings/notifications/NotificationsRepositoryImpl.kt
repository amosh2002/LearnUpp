package com.learnupp.data.settings.notifications

import com.learnupp.domain.repo.NotificationsRepository
class NotificationsRepositoryImpl(
    private val api: NotificationsApi,
    private val storage: NotificationsStorage
) : NotificationsRepository {

    override fun getSettings() =
        storage.getSettings()

    override suspend fun refreshData() {
        val list = api.fetchSettings()
        storage.save(list)
    }

    override suspend fun needsRefresh(): Boolean = storage.getSettings().value.isEmpty()

    override suspend fun toggleSetting(id: String, enabled: Boolean) {
        val result = api.updateSetting(id, enabled)
        storage.save(result)
    }
}

