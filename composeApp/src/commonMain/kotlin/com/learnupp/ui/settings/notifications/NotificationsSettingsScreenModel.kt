package com.learnupp.ui.settings.notifications

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.model.settings.NotificationGroup
import com.learnupp.domain.model.settings.NotificationSetting
import com.learnupp.domain.usecase.notifications.GetNotificationSettingsUseCase
import com.learnupp.domain.usecase.notifications.ReloadNotificationSettingsUseCase
import com.learnupp.domain.usecase.notifications.ToggleNotificationSettingUseCase
import com.learnupp.domain.usecase.notifications.ToggleNotificationParams
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NotificationSectionUi(
    val title: String,
    val settings: List<NotificationSetting>
)

class NotificationsSettingsScreenModel(
    private val getSettings: GetNotificationSettingsUseCase,
    private val reloadSettings: ReloadNotificationSettingsUseCase,
    private val toggleSetting: ToggleNotificationSettingUseCase
) : BaseScreenModel() {

    val sections = getSettings()
        .map { list ->
            list.groupBy { it.group }.map { (group, settings) ->
                NotificationSectionUi(
                    title = when (group) {
                        NotificationGroup.GENERAL -> "General"
                        NotificationGroup.APP_ACTIVITY -> "App Activity"
                        NotificationGroup.PREFERENCES -> "Preferences"
                    },
                    settings = settings
                )
            }
        }
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptyList())

    init {
        screenModelScope.launch { reloadSettings() }
    }

    fun toggle(id: String, enabled: Boolean) {
        screenModelScope.launch {
            toggleSetting(ToggleNotificationParams(id, enabled))
        }
    }
}

