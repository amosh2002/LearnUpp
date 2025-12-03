package com.learnupp.domain.usecase.notifications

import com.learnupp.domain.model.settings.NotificationSetting
import com.learnupp.domain.repo.NotificationsRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetNotificationSettingsUseCase : ParameterlessUseCase<Flow<List<NotificationSetting>>>()

class GetNotificationSettingsUseCaseImpl(
    private val repository: NotificationsRepository
) : GetNotificationSettingsUseCase() {
    override fun invoke(): Flow<List<NotificationSetting>> = repository.getSettings()
}

