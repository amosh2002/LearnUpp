package com.learnupp.ui.videos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.learnupp.domain.model.Video
import com.learnupp.safePush
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.courses.CoursesScreen
import com.learnupp.ui.widgets.RefreshableBox
import com.learnupp.ui.widgets.SearchFilterChipsSection
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch

class VideosScreen : BaseScreen(ScreenNameStrings.VIDEOS, hideTopAppBar = true) {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val screenModel: VideosScreenModel = koinScreenModel()

        val videos by screenModel.videos.collectAsState()
        val liked by screenModel.liked.collectAsState()

        // Create a mutable state for refresh status
        val isRefreshing = remember { mutableStateOf(false) }

        RefreshableBox(
            isRefreshing = isRefreshing.value,
            onRefresh = {
                isRefreshing.value = true
                screenModel.screenModelScope.launch {
                    screenModel.refresh()
                }.invokeOnCompletion {
                    isRefreshing.value = false
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                SearchAndChips()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(videos, key = { _, v -> v.id }) { index, video ->
                        if (index >= videos.lastIndex - 2) {
                            LaunchedEffect(index) { screenModel.loadMoreIfNeeded() }
                        }
                        VideoCard(
                            video = video,
                            isLiked = liked.contains(video.id),
                            onClick = {
                                navigator?.safePush(
                                    VideoDetailsScreen(
                                        title = video.title,
                                        url = video.fullVideoUrl
                                    )
                                )
                            },
                            onToggleLike = { screenModel.toggleLike(video.id) },
                            onShare = { screenModel.registerShare(video.id) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SearchAndChips() {
    SearchFilterChipsSection(
        placeholder = LearnUppStrings.SEARCH_COURSES_PLACEHOLDER.getValue(),
        chipLabels = listOf(
            LearnUppStrings.FITNESS_LABEL.getValue(),
            LearnUppStrings.DESIGN_LABEL.getValue(),
            LearnUppStrings.MARKETING_LABEL.getValue()
        ),
        seeAllLabel = LearnUppStrings.SEE_ALL.getValue(),
        modifier = Modifier.padding(bottom = 20.dp)
    )
}

@Composable
private fun VideoCard(
    video: Video,
    isLiked: Boolean,
    onClick: () -> Unit,
    onToggleLike: () -> Unit,
    onShare: () -> Unit
) {
    val navigator = LocalNavigator.current
    val isCoursePreview = video.courseUrl != null

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }
        ) {
            KamelImage(
                resource = asyncPainterResource(video.previewImageUrl),
                contentDescription = video.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
                contentScale = ContentScale.FillWidth,
                onFailure = { _ ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.06f))
                    )
                }
            )
            DurationPill(
                seconds = video.durationSec,
                modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = video.authorName.split(" ").mapNotNull { it.firstOrNull() }
                        .joinToString(""),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                if (isCoursePreview) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = LearnUppStrings.COURSE_PREVIEW_LABEL.getValue(),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = video.title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = video.channelName,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                )

                Spacer(modifier = Modifier.size(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${formatCount(video.viewsCount)} views",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.size(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onToggleLike() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Like",
                            tint = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary.copy(
                                alpha = 0.9f
                            ),
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.size(6.dp))

                        Text(
                            text = formatCount(video.likesCount + if (isLiked) 1 else 0),
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
            )
        }

        if (isCoursePreview) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        navigator?.safePush(CoursesScreen())
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = LearnUppStrings.VIEW_FULL_COURSE.getValue(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun DurationPill(seconds: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.Black.copy(alpha = 0.65f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = formatDuration(seconds),
            color = Color.White,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

private fun formatDuration(sec: Int): String {
    val m = sec / 60
    val s = sec % 60
    return m.toString().padStart(2, '0') + ":" + s.toString().padStart(2, '0')
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



