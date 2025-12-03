package com.learnupp.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ChipRow(
    labels: List<String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    seeAllLabel: String? = null,
    onChipClick: (Int) -> Unit = {},
    onSeeAllClick: (() -> Unit)? = null
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(labels) { index, label ->
            val selected = index == selectedIndex
            Surface(
                shape = RoundedCornerShape(30.dp),
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f),
                modifier = Modifier.clickable { onChipClick(index) }
            ) {
                Text(
                    text = label,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (seeAllLabel != null) {
            item {
                Text(
                    text = seeAllLabel,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 10.dp)
                        .clickable { onSeeAllClick?.invoke() },
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

