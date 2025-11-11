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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
                contentDescription = "Liked",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(120.dp)
            )
        }

        // 5) Bottom area - Instagram-style layout
        // Column with: 1) Row with metadata (left) and actions (right), 2) Optional "View Full Course" button at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1) Row with metadata (left) and action buttons (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Left: Author info and title
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Author row with avatar
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

                    // Course title
                    Text(
                        reel.title,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                // Right: Action buttons (like, comment, share)
                Column(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Like
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Like",
                            tint = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
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
                    ActionWithCount(
                        icon = Icons.Default.ChatBubble,
                        count = reel.commentsCount,
                        onClick = { /* Open comments */ }
                    )

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

                    // More options
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { /* Show more options menu */ }
                    )
                }
            }

            // 2) "View Full Course" button OR description + plus button
            // All options have exact same height to prevent layout shift
            if (reel.fullCourseId != null) {
                // Full course button replaces description + plus button
                Button(
                    onClick = { /* Open full course with ID: reel.fullCourseId */ },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Full Course", color = MaterialTheme.colorScheme.onPrimary)
                }
            } else {
                // Row with description (left) and plus button (right)
                // Height matches course button (48.dp) to prevent layout shift
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), // Match course button height
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Description text on the left (if available)
                    if (reel.description != null) {
                        Text(
                            text = reel.description,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp)
                        )
                    } else {
                        // Spacer to maintain layout when no description
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // Red plus button on the right
                    FloatingActionButton(
                        onClick = { /* Add to favorites or follow */ },
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp) // Match course button height
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionWithCount(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    onClick: (() -> Unit)? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(28.dp)
                .then(
                    if (onClick != null) {
                        Modifier.clickable { onClick() }
                    } else {
                        Modifier
                    }
                )
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
