package com.learnupp.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshableBox(
    onRefresh: () -> Unit,
    enabled: Boolean = true,
    isRefreshing: Boolean,
    content: @Composable () -> Unit
) {
    val pullToRefreshState = remember { PullToRefreshState() }

    if (enabled) {
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxWidth(),
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState,
                    containerColor = MaterialTheme.colorScheme.background,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            onRefresh = {
                onRefresh()
            },
            content = {
                content()
            }
        )
    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}