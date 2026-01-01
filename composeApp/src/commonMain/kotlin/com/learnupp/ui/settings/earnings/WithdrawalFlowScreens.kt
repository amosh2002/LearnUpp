package com.learnupp.ui.settings.earnings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.base.getValue
import com.learnupp.ui.widgets.PrimaryButton
import com.learnupp.util.PreferencesManager
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.learnupp.safePush
import org.koin.mp.KoinPlatform.getKoin

object WithdrawFundsScreen : BaseScreen(ScreenNameStrings.WITHDRAW_FUNDS) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val prefs: PreferencesManager = getKoin().get()
        val scope = rememberCoroutineScope()

        var isPinSet by remember { mutableStateOf(false) }
        var showAuthDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            isPinSet = prefs.getString(PIN_KEY) != null
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PayoutMethod(onClick = { navigator.safePush(PaymentMethodsScreen) })
                AmountToWithdraw()
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryButton(
                    text = "Withdraw",
                    onClick = { showAuthDialog = true },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedButton(
                    onClick = { navigator.pop() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Cancel")
                }
            }
        }

        if (showAuthDialog) {
            WithdrawAuthDialog(
                isPinSet = isPinSet,
                onDismiss = { showAuthDialog = false },
                onPinCreated = { pin ->
                    scope.launch {
                        prefs.saveString(PIN_KEY, pin)
                        isPinSet = true
                        showAuthDialog = false
                        navigator.safePush(WithdrawalSuccessfulScreen)
                    }
                },
                onPinVerified = { success ->
                    if (success) {
                        showAuthDialog = false
                        navigator.safePush(WithdrawalSuccessfulScreen)
                    }
                },
                prefs = prefs,
                scope = scope
            )
        }
    }
}

object PaymentMethodsScreen : BaseScreen(ScreenNameStrings.WITHDRAW_PAYOUT_METHODS) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("CURRENT METHOD", style = MaterialTheme.typography.labelMedium)
                PaymentMethodItem(
                    icon = "Visa",
                    details = "**** 4821",
                    primary = true,
                    selected = true
                )
                Text("SAVED METHODS", style = MaterialTheme.typography.labelMedium)
                PaymentMethodItem(icon = "PayPal", details = "johndoe@uxpilot.com")
                PaymentMethodItem(icon = "Mastercard", details = "Expires 12/26")
            }
            PrimaryButton(
                text = "Add Payment Method",
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

object WithdrawalSuccessfulScreen : BaseScreen(ScreenNameStrings.WITHDRAW_SUCCESS) {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Success",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Text(
                    screenName.getValue(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Your withdrawal request has been received. Funds will arrive in 1-3 business days.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Amount:")
                            Text("$243.10", fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Payout Method:")
                            Text("Visa •••• 4821", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                PrimaryButton(
                    text = "Close",
                    onClick = { navigator.popUntilRoot() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}

@Composable
private fun PayoutMethod(onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Visa",
                    modifier = Modifier.background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Column {
                    Text("Visa •••• 4821", fontWeight = FontWeight.Bold)
                    Text("Primary withdrawal method", style = MaterialTheme.typography.bodySmall)
                }
            }
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Select method",
                modifier = Modifier.graphicsLayer(rotationZ = 180f)
            )
        }
    }
}

@Composable
private fun AmountToWithdraw() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Amount to withdraw",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "$0.00",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )
            Surface(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Available: $243.10",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    "No withdrawal fees. Processing takes 1-3 business days.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WithdrawAuthDialog(
    isPinSet: Boolean,
    onDismiss: () -> Unit,
    onPinCreated: (String) -> Unit,
    onPinVerified: (Boolean) -> Unit,
    prefs: PreferencesManager,
    scope: CoroutineScope
) {
    var pinInput by remember { mutableStateOf("") }
    var confirmPinInput by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val minPinLength = 4

    fun resetFields() {
        pinInput = ""
        confirmPinInput = ""
        error = null
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                if (isPinSet) LearnUppStrings.ACCOUNT_AND_LOGIN.getValue()
                else LearnUppStrings.CHANGE_EMAIL.getValue()
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (isPinSet) {
                    Text(
                        text = "Enter your withdrawal PIN to continue.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            if (it.length <= 6) pinInput = it.filter { ch -> ch.isDigit() }
                        },
                        label = { Text("PIN") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    )
                    OutlinedButton(
                        onClick = {
                            error = LearnUppStrings.COMING_SOON.getValue()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Use Face ID / Biometrics")
                    }
                } else {
                    Text(
                        text = "Set a 4-6 digit PIN for withdrawals. You’ll need it to proceed.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            if (it.length <= 6) pinInput = it.filter { ch -> ch.isDigit() }
                        },
                        label = { Text("New PIN") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    )
                    OutlinedTextField(
                        value = confirmPinInput,
                        onValueChange = {
                            if (it.length <= 6) confirmPinInput = it.filter { ch -> ch.isDigit() }
                        },
                        label = { Text("Confirm PIN") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    )
                }

                error?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                error = null
                if (isPinSet) {
                    scope.launch {
                        val saved = prefs.getString(PIN_KEY)
                        if (!saved.isNullOrEmpty() && saved == pinInput) {
                            onPinVerified(true)
                            resetFields()
                        } else {
                            error = "Incorrect PIN"
                        }
                    }
                } else {
                    if (pinInput.length < minPinLength) {
                        error = "PIN must be at least $minPinLength digits"
                        return@TextButton
                    }
                    if (pinInput != confirmPinInput) {
                        error = "PINs do not match"
                        return@TextButton
                    }
                    onPinCreated(pinInput)
                    resetFields()
                }
            }) {
                Text("Continue")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                resetFields()
                onDismiss()
            }) { Text("Cancel") }
        }
    )
}

private const val PIN_KEY = "withdraw_pin_value"

@Composable
private fun PaymentMethodItem(
    icon: String,
    details: String,
    primary: Boolean = false,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    icon,
                    modifier = Modifier.background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Column {
                    Text(details, fontWeight = FontWeight.Bold)
                    if (primary) {
                        Text(
                            "Primary withdrawal method",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (selected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Select",
                    modifier = Modifier.graphicsLayer(rotationZ = 180f)
                )
            }
        }
    }
}
