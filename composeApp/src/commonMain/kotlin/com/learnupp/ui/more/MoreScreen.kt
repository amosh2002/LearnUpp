package com.learnupp.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.learnupp.ui.widgets.LoadingScreen
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import kotlinx.coroutines.launch

class MoreScreen : BaseScreen(ScreenNameStrings.MORE) {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val dialogState = LocalDialogState.current

        val moreScreenModel: MoreScreenModel = koinScreenModel()

        val isLoading by moreScreenModel.isLoading.collectAsState()


        if (isLoading) {
            LoadingScreen()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            ProfileHeader(
                name = "Jane Doe",
                email = "jane@example.com"
            )

            Spacer(Modifier.height(16.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))) {
                Column(Modifier.fillMaxWidth()) {
                    MoreRowItem("Account")
                    HorizontalDivider()
                    MoreRowItem("Settings")
                    HorizontalDivider()
                    MoreRowItem("Help & Support")
                    HorizontalDivider()
                    MoreRowItem(
                        "Logout",
                        onClick = {
                            dialogState.value = DialogParams(
                                title = LearnUppStrings.LOG_OUT_TITLE.getValue(),
                                message = "",
                                dialogType = DialogType.QUESTION,
                                buttonCount = 2,
                                confirmText = LearnUppStrings.LOG_OUT.getValue(),
                                dismissText = LearnUppStrings.CANCEL.getValue(),
                                onConfirm = {
                                    // Dismiss the dialog and open settings.
                                    dialogState.value = null
                                    moreScreenModel.screenModelScope.launch {
                                        val success = moreScreenModel.logout()
                                        if (success) {
                                            // If sign-out is successful, navigate to the Login screen
                                            navigator.replaceAll(LoginScreen())
                                        } else {
                                            // Optionally show a failure dialog if logout failed
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
                                onDismiss = {
                                    // Simply dismiss the dialog.
                                    dialogState.value = null
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(name: String, email: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFD1FAE5))
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = email, color = Color(0xFF64748B))
        }
    }
}

@Composable
private fun MoreRowItem(title: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = MaterialTheme.colorScheme.onBackground)
        Text(text = "›", color = MaterialTheme.colorScheme.primary)
    }
}


