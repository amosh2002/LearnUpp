package com.learnupp.ui.settings.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings

data class HelpAction(
    val title: String,
    val subtitle: String
)

class HelpSupportScreen : BaseScreen(ScreenNameStrings.HELP) {
    @Composable
    override fun Content() {
        val actions = listOf(
            HelpAction("Contact Support", "support@learnupp.com"),
            HelpAction("Community Forum", "Best practices and tips"),
            HelpAction("Report a Problem", "Let us know if something breaks")
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {
                items(actions) { action ->
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = action.title, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = action.subtitle,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "We respond within 24 hours.", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
        }
    }
}

