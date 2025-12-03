package com.learnupp.ui.settings.payments

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import com.learnupp.domain.model.payments.PaymentMethod
import com.learnupp.domain.model.payments.PaymentMethodType
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.widgets.PrimaryButton

class PaymentMethodsScreen : BaseScreen(ScreenNameStrings.PAYMENTS) {
    @Composable
    override fun Content() {
        val model: PaymentMethodsScreenModel = koinScreenModel()
        val methods by model.methods.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            methods.firstOrNull { it.primary }?.let {
                ActiveMethodCard(it)
            }

            PrimaryButton(
                text = "+ Add Payment Method",
                modifier = Modifier.fillMaxWidth(),
                buttonColors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(18.dp),
                height = 54.dp,
                onClick = { /* TODO: Implement adding payment method */ }
            )

            Text(
                text = "Saved Methods",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(methods.filter { !it.primary }) { method ->
                    SavedMethodRow(method)
                }
            }
        }
    }
}

@Composable
private fun ActiveMethodCard(method: PaymentMethod) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(26.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardLogo(method.type, method.label)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = method.label, fontWeight = FontWeight.Bold)
                    Text(
                        text = method.subtitle,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                }
                StatusPill(text = "ACTIVE", color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = method.maskedNumber,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF00C853)
                )
                Text(
                    text = "Primary Withdrawal Method",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun SavedMethodRow(method: PaymentMethod) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardLogo(method.type, method.label)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = method.label, fontWeight = FontWeight.SemiBold)
                Text(
                    text = method.subtitle,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            }
            if (method.maskedNumber.isNotBlank()) {
                Text(
                    text = method.maskedNumber,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun CardLogo(type: PaymentMethodType, label: String = "") {
    val bgColor = when (type) {
        PaymentMethodType.CARD -> Color(0xFF1E88E5)
        PaymentMethodType.PAYPAL -> Color(0xFF003087)
    }
    val text = when {
        type == PaymentMethodType.PAYPAL -> "PP"
        label.contains("visa", ignoreCase = true) -> "VISA"
        label.contains("master", ignoreCase = true) -> "MC"
        else -> "CARD"
    }
    Surface(
        shape = CircleShape,
        color = bgColor,
        modifier = Modifier.size(44.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StatusPill(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = text,
            color = color,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

