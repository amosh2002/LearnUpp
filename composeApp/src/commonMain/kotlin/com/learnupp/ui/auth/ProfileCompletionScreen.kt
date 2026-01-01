package com.learnupp.ui.auth

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.videos.VideosScreen
import com.learnupp.ui.widgets.LoadingScreen
import com.learnupp.ui.widgets.PrimaryButton
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class UsernameStatus { Idle, Checking, Available, Taken, Error }

class ProfileCompletionScreen : BaseScreen(
    ScreenNameStrings.PROFILE_SETUP,
    hideTopAppBar = true,
    hideBottomNavBar = true
) {
    @Composable
    override fun Content() {
        val screenModel: ProfileCompletionScreenModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()

        var username by remember { mutableStateOf("") }
        var fullName by remember { mutableStateOf("") }
        var acceptedTerms by remember { mutableStateOf(false) }
        var status by remember { mutableStateOf(UsernameStatus.Idle) }
        var errorText by remember { mutableStateOf<String?>(null) }
        var debounceJob by remember { mutableStateOf<Job?>(null) }
        val isLoading by screenModel.isLoading.collectAsState()

        if (isLoading) LoadingScreen()

        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
        )

        LaunchedEffect(username) {
            errorText = null
            if (username.isBlank()) {
                status = UsernameStatus.Idle
                return@LaunchedEffect
            }
            status = UsernameStatus.Checking
            debounceJob?.cancel()
            debounceJob = scope.launch {
                delay(2000)
                val available = runCatching { screenModel.checkUsername(username) }.getOrDefault(false)
                status = if (available) UsernameStatus.Available else UsernameStatus.Taken
            }
        }

        fun cleanUsername(raw: String): String = raw.trim().removePrefix("@")

        fun submit() {
            val normalized = cleanUsername(username)
            if (normalized.isBlank()) {
                errorText = LearnUppStrings.USERNAME_LABEL.getValue()
                return
            }
            if (!acceptedTerms) {
                errorText = LearnUppStrings.REGISTER_TERMS_TEXT.getValue()
                return
            }
            if (status == UsernameStatus.Taken) {
                errorText = LearnUppStrings.USERNAME_TAKEN.getValue()
                return
            }
            scope.launch {
                val success = screenModel.completeProfile(normalized, fullName.ifBlank { null })
                if (success) {
                    navigator.replaceAll(VideosScreen())
                } else {
                    errorText = LearnUppStrings.SOMETHING_WENT_WRONG.getValue()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = LearnUppStrings.PROFILE_SETUP_TITLE.getValue(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = LearnUppStrings.PROFILE_SETUP_SUBTITLE.getValue(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text(LearnUppStrings.USERNAME_LABEL.getValue()) },
                        placeholder = { Text(LearnUppStrings.USERNAME_PLACEHOLDER.getValue()) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = textFieldColors
                    )
                    when (status) {
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
                        label = { Text(LearnUppStrings.FULL_NAME_OPTIONAL.getValue()) },
                        placeholder = { Text(LearnUppStrings.REGISTER_FULL_NAME_PLACEHOLDER.getValue()) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = textFieldColors
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = LearnUppStrings.REGISTER_TERMS_TEXT.getValue(),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }

                errorText?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                PrimaryButton(
                    text = LearnUppStrings.COMPLETE_PROFILE_BUTTON.getValue(),
                    modifier = Modifier.fillMaxWidth(),
                    height = 56.dp,
                    shape = RoundedCornerShape(26.dp),
                    enabled = username.isNotBlank() && acceptedTerms && status != UsernameStatus.Taken,
                    onClick = { submit() }
                )
            }
        }
    }
}

