package com.learnupp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.learnupp.ui.home.HomeScreen
import com.learnupp.ui.widgets.LoadingScreen
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import kotlinx.coroutines.launch

class LoginScreen : BaseScreen(ScreenNameStrings.LOGIN) {
    @Composable
    override fun Content() {
        val screenModel: LoginScreenModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val dialogState = LocalDialogState.current

        
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        val isLoading by screenModel.isLoading.collectAsState()

        if (isLoading) {
            LoadingScreen()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1565C0), Color(0xFF2E7D32))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome back",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Sign in to continue",
                        color = Color(0xFF64748B)
                    )
                    Spacer(Modifier.height(20.dp))
                    
                    Spacer(Modifier.height(20.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            screenModel.screenModelScope.launch {
                                val errorMessage =
                                    screenModel.login(
                                        email, password
                                    )

                                // Show error dialog if there's an error
                                if (errorMessage != null) {
                                    dialogState.value = DialogParams(
                                        title = LearnUppStrings.FAILED_TO_LOG_IN.getValue(),
                                        message = errorMessage,
                                        dialogType = DialogType.ERROR,
                                        buttonCount = 1,
                                        confirmText = LearnUppStrings.TRY_AGAIN.getValue(),
                                        onConfirm = {
                                            dialogState.value = null
                                        }, // Dismiss dialog
                                        onDismiss = {
                                            dialogState.value = null
                                        }  // Dismiss dialog
                                    )
                                } else {
                                    navigator.replaceAll(HomeScreen())
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                    ) { Text("Login") }
                }
            }
        }
    }
}


