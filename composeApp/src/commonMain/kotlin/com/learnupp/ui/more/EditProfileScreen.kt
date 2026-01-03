package com.learnupp.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.learnupp.DialogParams
import com.learnupp.DialogType
import com.learnupp.LocalDialogState
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.widgets.LoadingScreen
import com.learnupp.ui.widgets.PrimaryButton
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class UsernameStatus { Idle, Checking, Available, Taken, Original }

class EditProfileScreen : BaseScreen(
    ScreenNameStrings.EDIT_PROFILE,
    hideBottomNavBar = true
) {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val dialogState = LocalDialogState.current
        val scope = rememberCoroutineScope()

        val moreScreenModel: MoreScreenModel = koinScreenModel()
        val profile by moreScreenModel.profile.collectAsState()

        var fullName by remember { mutableStateOf("") }
        var bio by remember { mutableStateOf("") }
        var hasPrefilled by remember { mutableStateOf(false) }
        var usernameStatus by remember { mutableStateOf(UsernameStatus.Idle) }
        var usernameError by remember { mutableStateOf<String?>(null) }
        var isSaving by remember { mutableStateOf(false) }
        var debounceJob by remember { mutableStateOf<Job?>(null) }
        var username by remember { mutableStateOf(profile.username) }

        val scrollState = rememberScrollState()

        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
        )

        fun cleanUsername(raw: String): String = raw.trim().removePrefix("@")

        LaunchedEffect(profile.userId) {
            if (profile.userId.isNotBlank()) {
                username = cleanUsername(profile.username)
                fullName = profile.fullName.orEmpty()
                bio = profile.about
                hasPrefilled = true
            }
        }

        LaunchedEffect(username) {
            usernameError = null
            if (username.isBlank() || username == cleanUsername(profile.username)) {
                usernameStatus = UsernameStatus.Idle
                return@LaunchedEffect
            }
            usernameStatus = UsernameStatus.Checking
            debounceJob?.cancel()
            debounceJob = scope.launch {
                delay(2000)
                val available =
                    runCatching { moreScreenModel.checkUsername(username) }.getOrDefault(false)
                usernameStatus = if (available) UsernameStatus.Available else UsernameStatus.Taken
            }
        }


        fun showComingSoon() {
            dialogState.value = DialogParams(
                title = LearnUppStrings.CHANGE_PHOTO.getValue(),
                message = LearnUppStrings.PHOTO_CHANGE_COMING_SOON.getValue(),
                dialogType = DialogType.INFO,
                buttonCount = 1,
                confirmText = LearnUppStrings.OK.getValue(),
                onConfirm = { dialogState.value = null },
                onDismiss = { dialogState.value = null }
            )
        }

        fun attemptSave() {
            val trimmedFullName = fullName.trim()
            val trimmedBio = bio.trim()
            val finalUsername = cleanUsername(username)
            if (finalUsername.isBlank()) {
                usernameError = LearnUppStrings.USERNAME_LABEL.getValue()
                return
            }
            if (usernameStatus == UsernameStatus.Taken) {
                usernameError = LearnUppStrings.USERNAME_TAKEN.getValue()
                return
            }
            if (usernameStatus == UsernameStatus.Checking) {
                return
            }
            isSaving = true
            scope.launch {
                val success = moreScreenModel.updateProfileInfo(
                    username = finalUsername,
                    fullName = trimmedFullName.ifBlank { null },
                    about = trimmedBio
                )
                isSaving = false
                if (success) {
                    navigator.pop()
                } else {
                    dialogState.value = DialogParams(
                        title = LearnUppStrings.ERROR.getValue(),
                        message = LearnUppStrings.PROFILE_UPDATE_FAILED.getValue(),
                        dialogType = DialogType.ERROR,
                        buttonCount = 1,
                        confirmText = LearnUppStrings.OK.getValue(),
                        onConfirm = { dialogState.value = null },
                        onDismiss = { dialogState.value = null }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AvatarSection(
                    avatarUrl = profile.avatarUrl,
                    onChangePhoto = { showComingSoon() }
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = LearnUppStrings.EDIT_PROFILE_TITLE.getValue(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        OutlinedTextField(
                            value = username,
                            onValueChange = { input ->
                                // Always keep internal value without leading '@'
                                username = input.removePrefix("@")
                            },
                            label = { Text(LearnUppStrings.USERNAME_LABEL.getValue()) },
                            placeholder = { Text(LearnUppStrings.USERNAME_PLACEHOLDER.getValue()) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            leadingIcon = {
                                Text(
                                    text = "@",
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            colors = textFieldColors
                        )
                        when (usernameStatus) {
                            UsernameStatus.Checking -> Text(
                                LearnUppStrings.USERNAME_CHECKING.getValue(),
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.bodySmall
                            )

                            UsernameStatus.Available -> Text(
                                LearnUppStrings.USERNAME_AVAILABLE.getValue(),
                                color = Color(0xFF4CAF50),
                                style = MaterialTheme.typography.bodySmall
                            )

                            UsernameStatus.Taken -> Text(
                                LearnUppStrings.USERNAME_TAKEN.getValue(),
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )

                            else -> {}
                        }

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text(LearnUppStrings.PROFILE_FULL_NAME_LABEL.getValue()) },
                            placeholder = { Text(LearnUppStrings.REGISTER_FULL_NAME_PLACEHOLDER.getValue()) },
                            colors = textFieldColors,
                            shape = RoundedCornerShape(18.dp),
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                        )

                        OutlinedTextField(
                            value = profile.email.orEmpty(),
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            label = { Text(LearnUppStrings.PROFILE_EMAIL_LABEL.getValue()) },
                            colors = textFieldColors,
                            shape = RoundedCornerShape(18.dp)
                        )

                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            label = { Text(LearnUppStrings.PROFILE_BIO_LABEL.getValue()) },
                            placeholder = { Text(LearnUppStrings.PROFILE_BIO_PLACEHOLDER.getValue()) },
                            colors = textFieldColors,
                            shape = RoundedCornerShape(20.dp),
                            maxLines = 6
                        )
                    }
                }

                PrimaryButton(
                    text = LearnUppStrings.SAVE_CHANGES.getValue(),
                    modifier = Modifier.fillMaxWidth(),
                    height = 56.dp,
                    shape = RoundedCornerShape(26.dp),
                    enabled = !isSaving && cleanUsername(username).isNotBlank() && usernameStatus != UsernameStatus.Taken && usernameStatus != UsernameStatus.Checking,
                    onClick = { attemptSave() }
                )

                TextButton(onClick = { if (!isSaving) navigator.pop() }) {
                    Text(
                        text = LearnUppStrings.CANCEL.getValue(),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isSaving) {
                LoadingScreen()
            }
        }
    }
}

@Composable
private fun AvatarSection(avatarUrl: String?, onChangePhoto: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (avatarUrl.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        } else {
            KamelImage(
                resource = asyncPainterResource(avatarUrl),
                contentDescription = LearnUppStrings.PROFILE_AVATAR.getValue(),
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )
        }
        Text(
            text = LearnUppStrings.CHANGE_PHOTO.getValue(),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onChangePhoto() }
        )
    }
}