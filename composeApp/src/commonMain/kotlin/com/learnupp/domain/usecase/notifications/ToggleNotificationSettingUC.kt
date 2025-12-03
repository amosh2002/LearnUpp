package com.learnupp.domain.usecase.notifications

import com.learnupp.domain.repo.NotificationsRepository
import com.learnupp.domain.usecase.base.BaseSuspendUseCase

data class ToggleNotificationParams(val id: String, val enabled: Boolean)

abstract class ToggleNotificationSettingUseCase :
    BaseSuspendUseCase<ToggleNotificationParams, Unit>()

class ToggleNotificationSettingUseCaseImpl(
    private val repository: NotificationsRepository
) : ToggleNotificationSettingUseCase() {
    override suspend fun invoke(params: ToggleNotificationParams) {
        repository.toggleSetting(params.id, params.enabled)
    }
}

