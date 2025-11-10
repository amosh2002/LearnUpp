package com.learnupp.ui.reels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import com.learnupp.domain.model.Reel
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.video.PlatformVideoPlayer
import com.learnupp.ui.video.prefetchVideo
import com.learnupp.util.LearnUppNonPrimaryColors
import com.learnupp.util.openShareSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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

        // Smooth snap/fling. PagerDefaults gives good feel across platforms.
        val pagerState = rememberPagerState(pageCount = { reels.size }, initialPage = 0)
        val flingBehavior = PagerDefaults.flingBehavior(state = pagerState)

        // Local pause state per-reel (id -> paused)
        val pausedMap = remember { mutableStateMapOf<String, Boolean>() }

        LaunchedEffect(pagerState.currentPage, reels.size) {
            // Load more a bit before the end
            if (pagerState.currentPage >= reels.size - 2) viewModel.loadMore()
            // Prefetch next video to reduce startup lag
            reels.getOrNull(pagerState.currentPage + 1)?.let { prefetchVideo(it.videoUrl) }
        }

        VerticalPager(
            state = pagerState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(0.dp),
            beyondViewportPageCount = 1,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) { page ->
            val reel = reels[page]
            val isPaused = pausedMap[reel.id] == true
            val play = (page == pagerState.currentPage) && !isPaused

            ReelItem(
                reel = reel,
                isLiked = liked.contains(reel.id),
                isPlaying = play,
                onTogglePlay = { pausedMap[reel.id] = !(pausedMap[reel.id] ?: false) },
                onToggleReelLike = { viewModel.toggleReelLike(reel.id) },
                onShare = {
                    viewModel.shareReel(reel.id)
                    openShareSheet(
                        text = "Check out this course short: ${reel.title} by ${reel.authorName}",
                        url = reel.videoUrl
                    )
                }
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

    // Heart animation visibility + where to show it (tap position)
    val heartVisible = remember { mutableStateOf(false) }
    val heartPos = remember { mutableStateOf(Offset.Zero) }

    Box(modifier = Modifier.fillMaxSize()) {

        // 1) Fullscreen video
        PlatformVideoPlayer(
            url = reel.videoUrl,
            playVideoWhenReady = isPlaying,
            modifier = Modifier.fillMaxSize(),
            onClicked = null // we handle taps in the overlay below for single/double tap
        )

        // 2) Gesture overlay (does not block vertical drag -> pager remains smooth)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(reel.id) {
                    // detectTapGestures properly recognizes double-tap vs single-tap.
                    detectTapGestures(
                        onDoubleTap = { offset ->
                            // Show heart at tap position
                            heartPos.value = offset
                            onToggleReelLike()
                            heartVisible.value = true
                            scope.launch {
                                delay(700)
                                heartVisible.value = false
                            }
                        },
                        onTap = {
                            // Single tap toggles play/pause
                            onTogglePlay()
                        }
                    )
                }
        )

        // 3) Heart animation shown where the user double-tapped
        AnimatedVisibility(
            visible = heartVisible.value,
            enter = fadeIn(animationSpec = tween(120, easing = FastOutSlowInEasing)) +
                    scaleIn(initialScale = 0.6f, animationSpec = tween(180)),
            exit = fadeOut(animationSpec = tween(220)) +
                    scaleOut(targetScale = 0.6f, animationSpec = tween(220)),
            modifier = Modifier
                .offset {
                    // Convert tap position (px) -> IntOffset for placement
                    IntOffset(
                        x = heartPos.value.x.roundToInt() - 60, // center 120dp-ish icon
                        y = heartPos.value.y.roundToInt() - 60
                    )
                }
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = LearnUppNonPrimaryColors.RED,
                modifier = Modifier.size(120.dp)
            )
        }

        // 4) Title overlay (optional)
        Text(
            text = "Course Video Short",
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.92f),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp)
        )

        // 5) Right action rail
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Like
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    tint = if (isLiked) LearnUppNonPrimaryColors.RED else MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onToggleReelLike() }
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = formatCount(reel.likesCount + if (isLiked) 1 else 0),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Comments
            ActionWithCount(icon = Icons.Default.ChatBubble, count = reel.commentsCount)

            // Share
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onShare() }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Share", color = MaterialTheme.colorScheme.onPrimary)
            }

            // Plus
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

        // 6) Bottom metadata & CTA
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = reel.authorName.split(" ").mapNotNull { it.firstOrNull() }
                            .joinToString(""),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(
                        reel.authorName,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        reel.authorTitle,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                reel.title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Open full course later */ },
                colors = ButtonDefaults.buttonColors(containerColor = LearnUppNonPrimaryColors.RED),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Full Course", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
private fun ActionWithCount(icon: androidx.compose.ui.graphics.vector.ImageVector, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = formatCount(count), color = MaterialTheme.colorScheme.onPrimary)
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
