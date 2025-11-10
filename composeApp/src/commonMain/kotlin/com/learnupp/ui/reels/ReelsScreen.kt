package com.learnupp.ui.reels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import com.learnupp.domain.model.Reel
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import com.learnupp.ui.video.PlatformVideoPlayer
import com.learnupp.ui.video.prefetchVideo
import com.learnupp.util.openShareSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReelsScreen : BaseScreen(ScreenNameStrings.REELS) {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val viewModel: ReelsScreenModel = koinScreenModel()
        val reels by viewModel.reels.collectAsState()
        val liked by viewModel.liked.collectAsState()

        if (reels.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading Reels...", color = MaterialTheme.colorScheme.onBackground)
            }
            return
        }

        val pagerState = rememberPagerState(pageCount = { reels.size }, initialPage = 0)
        val pausedMap = remember { mutableStateMapOf<String, Boolean>() }

        LaunchedEffect(pagerState.currentPage, reels.size) {
            // Auto-load more when 2 from the end
            val nearEnd = pagerState.currentPage >= reels.size - 2
            if (nearEnd) viewModel.loadMore()
            // Prefetch next video to reduce start latency
            reels.getOrNull(pagerState.currentPage + 1)?.let { prefetchVideo(it.videoUrl) }
        }

        VerticalPager(
            state = pagerState,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) { page ->
            val reel = reels[page]
            val isPaused = pausedMap[reel.id] == true
            val play = page == pagerState.currentPage && !isPaused
            ReelItem(
                reel = reel,
                isLiked = liked.contains(reel.id),
                isPlaying = play,
                onTogglePlay = { pausedMap[reel.id] = !(pausedMap[reel.id] ?: false) },
                onToggleReelLike = { viewModel.toggleReelLike(reel.id) },
                onShare = { viewModel.shareReel(reel.id) }
            )
        }
    }
}

@Composable
private fun ReelItem(
    reel: Reel,
    isLiked: Boolean,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onToggleReelLike: () -> Unit,
    onShare: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val heartVisible = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Video fills the screen; tap toggles pause/play
        PlatformVideoPlayer(
            url = reel.videoUrl,
            playWhenReady = isPlaying,
            modifier = Modifier.fillMaxSize(),
            onClicked = { onTogglePlay() }
        )

        // Gesture overlay to handle single tap (play/pause) and double tap (like + animation)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(reel.id) {
                    detectTapGestures(
                        onTap = { onTogglePlay() },
                        onDoubleTap = {
                            onToggleReelLike()
                            heartVisible.value = true
                            scope.launch {
                                delay(700)
                                heartVisible.value = false
                            }
                        }
                    )
                }
        )

        AnimatedVisibility(
            visible = heartVisible.value,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFFF4D4D),
                modifier = Modifier.size(120.dp)
            )
        }

        // Center title overlay
        Text(
            text = "Course Video Short",
            color = Color.White.copy(alpha = 0.92f),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp)
        )

        // Right action rail
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Like button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    tint = if (isLiked) Color(0xFFFF4D4D) else Color.White,
                    modifier = Modifier
                        .clickable { onToggleReelLike() }
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = formatCount(reel.likesCount + if (isLiked) 1 else 0),
                    color = Color.White
                )
            }
            ActionWithCount(icon = Icons.Default.ChatBubble, count = reel.commentsCount)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        onShare()
                        openShareSheet(
                            text = "Check out this course short: ${reel.title} by ${reel.authorName}",
                            url = reel.videoUrl
                        )
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Share", color = Color.White)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        // Bottom info & CTA
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Author row
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Simple circular placeholder avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(Color.White.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = reel.authorName.split(" ").map { it.firstOrNull() ?: ' ' }.joinToString(""),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(reel.authorName, color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Text(reel.authorTitle, color = Color.White.copy(alpha = 0.9f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                reel.title,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { /* Open full course later */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD71C1C)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Full Course")
            }
        }
    }
}

@Composable
private fun ActionWithCount(icon: androidx.compose.ui.graphics.vector.ImageVector, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = formatCount(count),
            color = Color.White
        )
    }
}

private fun formatCount(value: Int): String {
    return when {
        value >= 1_000_000 -> "${(value / 1_000_000.0).format1()}M"
        value >= 1_000 -> "${(value / 1_000.0).format1()}K"
        else -> value.toString()
    }
}

private fun Double.format1(): String {
    val rounded = kotlin.math.round(this * 10.0) / 10.0
    val s = rounded.toString()
    return if (s.endsWith(".0")) s.dropLast(2) else s
}


