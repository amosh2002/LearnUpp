package com.learnupp.domain.usecase.notifications

import com.learnupp.domain.repo.NotificationsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadNotificationSettingsUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadNotificationSettingsUseCaseImpl(
    private val repository: NotificationsRepository
) : ReloadNotificationSettingsUseCase() {
    override suspend fun invoke() {
        repository.refreshData()
    }
}

