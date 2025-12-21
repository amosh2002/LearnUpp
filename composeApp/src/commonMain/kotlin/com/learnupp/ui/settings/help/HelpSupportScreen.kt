package com.learnupp.ui.settings.help

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings

class HelpSupportScreen : BaseScreen(ScreenNameStrings.HELP) {
    @Composable
    override fun Content() {
        val primaryActions = listOf(
            SupportAction(Icons.Outlined.HelpOutline, "FAQ", "Common questions"),
            SupportAction(Icons.Outlined.Chat, "Contact Support", "Chat with us"),
            SupportAction(Icons.Outlined.WarningAmber, "Report a Problem", "Bug reports")
        )

        val quickLinks = listOf(
            QuickLink(Icons.Outlined.HelpOutline, "User Guide", "How to use the app"),
            QuickLink(Icons.Outlined.Security, "Privacy", "Terms & Data")
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    primaryActions.forEachIndexed { index, action ->
                        SupportActionRow(action)
                        if (index != primaryActions.lastIndex) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "QUICK LINKS",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                quickLinks.forEach { link ->
                    QuickLinkCard(link, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            ResponseCard()
        }
    }
}

private data class SupportAction(
    val icon: ImageVector,
    val title: String,
    val subtitle: String
)

@Composable
private fun SupportActionRow(action: SupportAction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.title,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
            Text(text = action.title, fontWeight = FontWeight.SemiBold)
            Text(
                text = action.subtitle,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
            )
        }
        Icon(
            imageVector = Icons.Outlined.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
        )
    }
}

private data class QuickLink(
    val imageVector: ImageVector,
    val title: String,
    val subtitle: String
)

@Composable
private fun QuickLinkCard(link: QuickLink, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = link.title, fontWeight = FontWeight.Bold)
            Text(
                text = link.subtitle,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun ResponseCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Response Time",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Our support team typically responds within 24–48 hours.",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "For urgent billing issues, please check the FAQ first.",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

