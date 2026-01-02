package com.learnupp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.learnupp.domain.model.AuthProvider
import com.learnupp.safePush
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.widgets.LoadingScreen
import com.learnupp.ui.widgets.PrimaryButton
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import kotlinx.coroutines.launch

class AuthStartScreen : BaseScreen(
    ScreenNameStrings.AUTH_START,
    hideTopAppBar = true,
    hideBottomNavBar = true
) {
    @Composable
    override fun Content() {
        val screenModel: AuthStartScreenModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()

        var email by remember { mutableStateOf("") }
        var errorText by remember { mutableStateOf<String?>(null) }
        var infoText by remember { mutableStateOf<String?>(null) }
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

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = LearnUppStrings.AUTH_WELCOME_TITLE.getValue(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = LearnUppStrings.AUTH_WELCOME_SUBTITLE.getValue(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorText = null
                        },
                        label = { Text(LearnUppStrings.EMAIL_LABEL.getValue()) },
                        placeholder = { Text(LearnUppStrings.EMAIL_PLACEHOLDER.getValue()) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = textFieldColors
                    )

                    errorText?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    infoText?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    PrimaryButton(
                        text = LearnUppStrings.AUTH_SEND_CODE.getValue(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        height = 54.dp,
                        enabled = email.isNotBlank(),
                        onClick = {
                            val normalized = email.trim()
                            if (!normalized.contains("@")) {
                                errorText = LearnUppStrings.EMAIL_PLACEHOLDER.getValue()
                                return@PrimaryButton
                            }
                            scope.launch {
                                val result = screenModel.requestOtp(normalized)
                                if (result.success) {
                                    infoText = result.debugCode?.let { "DEV code: $it" }
                                    navigator.safePush(OtpScreen(normalized))
                                } else {
                                    errorText = LearnUppStrings.SOMETHING_WENT_WRONG.getValue()
                                }
                            }
                        }
                    )

                    Text(
                        text = LearnUppStrings.OR_CONTINUE_WITH.getValue(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AuthSocialButton(
                            text = LearnUppStrings.GOOGLE_LABEL.getValue(),
                            onClick = {
                                scope.launch {
                                    screenModel.loginWithProvider(AuthProvider.GOOGLE)
                                }
                            }
                        )
                        AuthSocialButton(
                            text = LearnUppStrings.APPLE_LABEL.getValue(),
                            onClick = {
                                scope.launch {
                                    screenModel.loginWithProvider(AuthProvider.APPLE)
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun AuthSocialButton(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 14.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

