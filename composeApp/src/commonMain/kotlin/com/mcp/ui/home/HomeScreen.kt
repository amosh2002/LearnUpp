package com.mcp.ui.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import com.mcp.domain.model.Camera
import com.mcp.ui.base.BaseScreen
import com.mcp.ui.base.ScreenNameStrings
import com.mcp.ui.widgets.LoadingFailScreen
import com.mcp.ui.widgets.LoadingScreen
import com.mcp.ui.widgets.RefreshableBox
import kotlinx.coroutines.launch

class HomeScreen : BaseScreen(ScreenNameStrings.HOME) {
    @Composable
    override fun Content() {
        val homeScreenModel: HomeScreenModel = koinScreenModel()

        val allCameras by homeScreenModel.allCameras.collectAsState()
        val featuredCamera by homeScreenModel.featuredCamera.collectAsState()

        val isLoading by homeScreenModel.isLoading.collectAsState()

        // Create a mutable state for refresh status
        val isRefreshing = remember { mutableStateOf(false) }

        // State to track if loading has failed (no connection) after 5 seconds.
        var loadingFailed by remember { mutableStateOf(false) }


        // Trigger a 5-second timeout when there are no cameras and not refreshing.
        LaunchedEffect(
            allCameras,
            isRefreshing,
        ) {
            // Reset the loadingFailed flag if services are available or if refresh is active.
            if (allCameras.isNotEmpty() || isRefreshing.value) {
                loadingFailed = false
            } else {
                // Wait for 4 seconds
                kotlinx.coroutines.delay(4000)
                // If still no services after delay, mark loading as failed.
                if (allCameras.isEmpty() && !isRefreshing.value) {
                    loadingFailed = true
                }
            }
        }

        if (loadingFailed) {
            // Show the LoadingFailScreen
            LoadingFailScreen(
                onRefreshClick = {
                    // Reset failure flag and trigger a refresh with timeout.
                    loadingFailed = false
                    isRefreshing.value = true
                    homeScreenModel.screenModelScope.launch {
                        homeScreenModel.refreshAllCameras()
                        homeScreenModel.refreshFeaturedCamera()
                    }.invokeOnCompletion {
                        isRefreshing.value = false
                        loadingFailed = allCameras.isEmpty()
                    }
                }
            )
        } else if (allCameras.isEmpty()) {
            // Show a loading indicator while waiting for data.
            LoadingScreen()
        } else {
            RefreshableBox(
                isRefreshing = isRefreshing.value,
                onRefresh = {
                    isRefreshing.value = true
                    homeScreenModel.screenModelScope.launch {
                        homeScreenModel.refreshAllCameras()
                        homeScreenModel.refreshFeaturedCamera()
                    }.invokeOnCompletion {
                        isRefreshing.value = false
                    }
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 16.dp)
                ) {
                    FeaturedCameraCard(featuredCamera)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Quick cameras",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        item { Spacer(Modifier.padding(start = 4.dp)) }
                        items(allCameras) { cam ->
                            CameraChip(cam)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedCameraCard(featuredCamera: Camera?) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFFEFF6FF))
        ) {
            Text(
                text = featuredCamera?.name ?: "No featured camera",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionPill(Icons.Default.Call, "Call")
            ActionPill(Icons.Default.Mic, "Mic")
            ActionPill(Icons.Default.Videocam, "Record")
        }
    }
}

@Composable
private fun CameraChip(camera: Camera) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
            Box(
                modifier = Modifier
                    .size(120.dp, 80.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(0xFFE2E8F0))
            )
            Spacer(Modifier.height(8.dp))
            Text(text = camera.name, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
private fun ActionPill(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Text(label, color = MaterialTheme.colorScheme.primary)
    }
}


