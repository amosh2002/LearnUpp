package com.learnupp.ui.videos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.video.PlatformVideoPlayer

class VideoDetailsScreen(
    private val title: String,
    private val url: String
) : BaseScreen(ScreenNameStrings.VIDEOS, hideBottomNavBar = true) {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((9 * 24).dp)
            ) {
                PlatformVideoPlayer(
                    url = url,
                    playVideoWhenReady = true,
                    modifier = Modifier.fillMaxSize(),
                    isMuted = false
                )
            }

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}



