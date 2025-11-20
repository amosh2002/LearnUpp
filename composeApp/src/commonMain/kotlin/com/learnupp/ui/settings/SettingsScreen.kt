package com.learnupp.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.learnupp.DialogParams
import com.learnupp.DialogType
import com.learnupp.LocalDialogState
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.login.LoginScreen
import com.learnupp.ui.more.MoreScreenModel
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import kotlinx.coroutines.launch

class SettingsScreen : BaseScreen(ScreenNameStrings.SETTINGS) {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val dialogState = LocalDialogState.current
        val moreScreenModel: MoreScreenModel = koinScreenModel()

        val logoutColor = MaterialTheme.colorScheme.error

        val items = listOf(
            SettingItem(LearnUppStrings.NOTIFICATIONS, Icons.Outlined.NotificationsNone),
            SettingItem(LearnUppStrings.PRIVACY, Icons.Outlined.PrivacyTip),
            SettingItem(LearnUppStrings.LANGUAGE, Icons.Outlined.Language),
            SettingItem(LearnUppStrings.DOWNLOAD_SETTINGS, Icons.Outlined.Download),
            SettingItem(LearnUppStrings.HELP_SUPPORT, Icons.Outlined.HelpOutline),
            SettingItem(
                label = LearnUppStrings.LOG_OUT,
                icon = Icons.Outlined.Logout,
                textColor = logoutColor,
                iconTint = logoutColor,
                onClick = {
                    dialogState.value = DialogParams(
                        title = LearnUppStrings.LOG_OUT_TITLE.getValue(),
                        message = "",
                        dialogType = DialogType.QUESTION,
                        buttonCount = 2,
                        confirmText = LearnUppStrings.LOG_OUT.getValue(),
                        dismissText = LearnUppStrings.CANCEL.getValue(),
                        onConfirm = {
                            dialogState.value = null
                            moreScreenModel.screenModelScope.launch {
                                val success = moreScreenModel.logout()
                                if (success) {
                                    navigator.replaceAll(LoginScreen())
                                } else {
                                    dialogState.value = DialogParams(
                                        title = LearnUppStrings.ERROR.getValue(),
                                        message = LearnUppStrings.FAILED_TO_LOG_OUT.getValue(),
                                        dialogType = DialogType.ERROR,
                                        buttonCount = 1,
                                        confirmText = LearnUppStrings.OK.getValue(),
                                        onConfirm = { dialogState.value = null },
                                        onDismiss = { dialogState.value = null }
                                    )
                                }
                            }
                        },
                        onDismiss = { dialogState.value = null }
                    )
                }
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(18.dp)
            ) {
                Column {
                    items.forEachIndexed { index, item ->
                        val labelText = item.label.getValue()
                        SettingsRow(
                            icon = item.icon,
                            label = labelText,
                            iconTint = item.iconTint ?: MaterialTheme.colorScheme.onPrimary,
                            textColor = item.textColor ?: MaterialTheme.colorScheme.onPrimary,
                            onClick = item.onClick ?: {}
                        )
                        if (index != items.lastIndex) {
                            SettingsDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    iconTint: Color,
    textColor: Color,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun SettingsDivider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f))
    )
}

private data class SettingItem(
    val label: LearnUppStrings,
    val icon: ImageVector,
    val textColor: Color? = null,
    val iconTint: Color? = null,
    val onClick: (() -> Unit)? = null,
)

