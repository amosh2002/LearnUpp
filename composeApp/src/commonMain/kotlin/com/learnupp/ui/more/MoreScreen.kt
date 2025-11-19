package com.learnupp.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.learnupp.domain.model.Profile
import com.learnupp.domain.model.Reel
import com.learnupp.domain.model.Video
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.settings.SettingsScreen
import com.learnupp.ui.widgets.LoadingScreen
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlin.math.roundToInt

class MoreScreen : BaseScreen(
    ScreenNameStrings.MORE,
    hideTopAppBar = true
) {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val moreScreenModel: MoreScreenModel = koinScreenModel()

        val isLoading by moreScreenModel.isLoading.collectAsState()
        val profile by moreScreenModel.profile.collectAsState()
        val videos by moreScreenModel.videos.collectAsState()
        val reels by moreScreenModel.reels.collectAsState()

        if (isLoading) {
            LoadingScreen()
        }

        ProfileScaffold(
            profile = profile,
            videos = videos,
            reels = reels,
            onLoadMoreVideos = { moreScreenModel.loadMoreForVideos() },
            onLoadMoreReels = { moreScreenModel.loadMoreForReels() },
            onEditAbout = { moreScreenModel.updateAbout(it) },
            onSettingsClick = { navigator.push(SettingsScreen()) }
        )
    }
}

private enum class ProfileTab { Videos, Shorts, Courses }

@Composable
private fun ProfileScaffold(
    profile: Profile,
    videos: List<Video>,
    reels: List<Reel>,
    onLoadMoreVideos: () -> Unit,
    onLoadMoreReels: () -> Unit,
    onEditAbout: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    var selectedTab by remember { mutableStateOf(ProfileTab.Videos) }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                ProfileHeader(profile, onSettingsClick)
                Spacer(Modifier.height(16.dp))
                StatsRow(profile)
                Spacer(Modifier.height(16.dp))
                AboutSection(
                    text = profile.about,
                    onEdit = onEditAbout
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        // Tabs
        item(span = { GridItemSpan(maxLineSpan) }) {
            TabsRow(
                selected = selectedTab,
                isLecturer = profile.isLecturer,
                onSelected = { selectedTab = it }
            )
        }

        when (selectedTab) {
            ProfileTab.Videos -> {
                itemsIndexed(videos, key = { _, v -> v.id }) { index, video ->
                    if (shouldTriggerLoad(index, videos.size)) {
                        LaunchedEffect(videos.size) { onLoadMoreVideos() }
                    }
                    GridThumb(url = video.previewImageUrl)
                }
            }

            ProfileTab.Shorts -> {
                itemsIndexed(reels, key = { _, r -> r.id }) { index, reel ->
                    if (shouldTriggerLoad(index, reels.size)) {
                        LaunchedEffect(reels.size) { onLoadMoreReels() }
                    }
                    GridThumb(url = reel.thumbnailUrl)
                }
            }

            ProfileTab.Courses -> {
                if (profile.isLecturer) {
                    itemsIndexed(videos, key = { _, v -> "course-${v.id}" }) { _, video ->
                        GridThumb(url = video.previewImageUrl)
                    }
                } else {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.06f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = LearnUppStrings.COURSES_LOCKED_MESSAGE.getValue(),
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun ProfileHeader(profile: Profile, onSettingsClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (profile.avatarUrl.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        } else {
            KamelImage(
                resource = asyncPainterResource(profile.avatarUrl!!),
                contentDescription = LearnUppStrings.PROFILE_AVATAR.getValue(),
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = profile.fullName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = profile.title,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
        }
        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = LearnUppStrings.SETTINGS_TITLE.getValue(),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun StatsRow(profile: Profile) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            value = formatCount(if (profile.isLecturer) profile.students else profile.followers),
            label = (
                if (profile.isLecturer) LearnUppStrings.STUDENTS_LABEL else LearnUppStrings.FOLLOWERS_LABEL
            ).getValue(),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = formatCount(if (profile.isLecturer) profile.courses else profile.following),
            label = (
                if (profile.isLecturer) LearnUppStrings.COURSES_LABEL else LearnUppStrings.FOLLOWING_LABEL
            ).getValue(),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = if (profile.isLecturer) formatRating(profile.rating) else formatCount(profile.posts),
            label = (
                if (profile.isLecturer) LearnUppStrings.RATING_LABEL else LearnUppStrings.POSTS_LABEL
            ).getValue(),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutSection(text: String, onEdit: (String) -> Unit) {
    var showEditor by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = LearnUppStrings.ABOUT_ME.getValue(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { showEditor = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = LearnUppStrings.EDIT_ABOUT.getValue(),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.92f),
            style = MaterialTheme.typography.bodyMedium
        )
    }

    if (showEditor) {
        var draft by remember(text) { mutableStateOf(text) }
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showEditor = false },
            title = { Text(LearnUppStrings.EDIT_ABOUT.getValue()) },
            text = {
                TextField(
                    value = draft,
                    onValueChange = { draft = it }
                )
            },
            confirmButton = {
                Text(
                    text = LearnUppStrings.SAVE.getValue(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable {
                            onEdit(draft)
                            showEditor = false
                        }
                )
            },
            dismissButton = {
                Text(
                    text = LearnUppStrings.CANCEL.getValue(),
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { showEditor = false }
                )
            }
        )
    }
}

@Composable
private fun TabsRow(
    selected: ProfileTab,
    isLecturer: Boolean,
    onSelected: (ProfileTab) -> Unit
) {
    val tabs = listOf(ProfileTab.Videos, ProfileTab.Shorts, ProfileTab.Courses)
    TabRow(
        selectedTabIndex = selected.ordinal,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selected.ordinal])
                    .height(2.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        tabs.forEach { tab ->
            val enabled = tab != ProfileTab.Courses || isLecturer
            Tab(
                selected = selected == tab,
                onClick = { if (enabled) onSelected(tab) },
                enabled = enabled,
                text = {
                    Text(
                        text = when (tab) {
                            ProfileTab.Videos -> LearnUppStrings.VIDEOS_LABEL
                            ProfileTab.Shorts -> LearnUppStrings.SHORTS_LABEL
                            ProfileTab.Courses -> LearnUppStrings.COURSES_LABEL
                        }.getValue(),
                        color = if (enabled) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f)
                    )
                }
            )
        }
    }
}

@Composable
private fun GridThumb(url: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.06f))
    ) {
        KamelImage(
            resource = asyncPainterResource(url),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
            onFailure = { _ ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.06f))
                )
            }
        )
    }
}

private fun formatCount(value: Number): String {
    val v = value.toDouble()
    return when {
        v >= 1_000_000 -> "${((v / 1_000_000.0) * 10).toInt() / 10.0}M"
        v >= 1_000 -> "${((v / 1_000.0) * 10).toInt() / 10.0}K"
        else -> value.toString()
    }
}

private fun formatRating(value: Double): String {
    val rounded = (value * 10).roundToInt() / 10.0
    return if (rounded % 1.0 == 0.0) rounded.toInt().toString() else rounded.toString()
}

private fun shouldTriggerLoad(index: Int, totalCount: Int): Boolean {
    if (totalCount == 0) return false
    val triggerIndex = (totalCount - 3).coerceAtLeast(0)
    return index == triggerIndex
}


