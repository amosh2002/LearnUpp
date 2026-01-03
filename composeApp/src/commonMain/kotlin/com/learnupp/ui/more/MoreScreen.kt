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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.material.icons.outlined.Logout
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.learnupp.DialogParams
import com.learnupp.DialogType
import com.learnupp.LocalDialogState
import com.learnupp.domain.model.Profile
import com.learnupp.domain.model.Reel
import com.learnupp.domain.model.Video
import com.learnupp.safePush
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.settings.earnings.EarningsScreen
import com.learnupp.ui.settings.help.HelpSupportScreen
import com.learnupp.ui.settings.language.LanguageSelectionScreen
import com.learnupp.ui.settings.notifications.NotificationsSettingsScreen
import com.learnupp.ui.settings.payments.PaymentMethodsScreen
import com.learnupp.ui.widgets.SettingsSheetOption
import com.learnupp.ui.widgets.LoadingScreen
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MoreScreen : BaseScreen(
    ScreenNameStrings.MORE,
    hideTopAppBar = true
) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val dialogState = LocalDialogState.current
        var showSettingsSheet by remember { mutableStateOf(false) }

        val moreScreenModel: MoreScreenModel = koinScreenModel()

        val isLoading by moreScreenModel.isLoading.collectAsState()
        val profile by moreScreenModel.profile.collectAsState()
        val videos by moreScreenModel.videos.collectAsState()
        val reels by moreScreenModel.reels.collectAsState()

        if (isLoading) {
            LoadingScreen()
        }

        if (showSettingsSheet) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showSettingsSheet = false },
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f))
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = LearnUppStrings.ACCOUNT_AND_LOGIN.getValue(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    SettingsSheetOption(
                        icon = Icons.Outlined.Email,
                        text = LearnUppStrings.CHANGE_EMAIL.getValue(),
                        iconBackground = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                    ) {
                        dialogState.value = DialogParams(
                            title = LearnUppStrings.CHANGE_EMAIL.getValue(),
                            message = LearnUppStrings.EMAIL_UPDATE_OTP_SENT.getValue(),
                            dialogType = DialogType.INFO,
                            buttonCount = 1,
                            confirmText = LearnUppStrings.OK.getValue(),
                            onConfirm = { dialogState.value = null },
                            onDismiss = { dialogState.value = null }
                        )
                        showSettingsSheet = false
                    }
                    SettingsSheetOption(
                        icon = Icons.Outlined.Link,
                        text = LearnUppStrings.LINK_GOOGLE.getValue(),
                        iconBackground = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                    ) {
                        dialogState.value = DialogParams(
                            title = LearnUppStrings.LINK_GOOGLE.getValue(),
                            message = LearnUppStrings.COMING_SOON.getValue(),
                            dialogType = DialogType.INFO,
                            buttonCount = 1,
                            confirmText = LearnUppStrings.OK.getValue(),
                            onConfirm = { dialogState.value = null },
                            onDismiss = { dialogState.value = null }
                        )
                        showSettingsSheet = false
                    }
                    SettingsSheetOption(
                        icon = Icons.Outlined.AccountCircle,
                        text = LearnUppStrings.LINK_APPLE.getValue(),
                        iconBackground = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                    ) {
                        dialogState.value = DialogParams(
                            title = LearnUppStrings.LINK_APPLE.getValue(),
                            message = LearnUppStrings.COMING_SOON.getValue(),
                            dialogType = DialogType.INFO,
                            buttonCount = 1,
                            confirmText = LearnUppStrings.OK.getValue(),
                            onConfirm = { dialogState.value = null },
                            onDismiss = { dialogState.value = null }
                        )
                        showSettingsSheet = false
                    }
                    SettingsSheetOption(
                        icon = Icons.Outlined.Settings,
                        text = LearnUppStrings.SET_AS_PRIMARY.getValue(),
                        iconBackground = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                    ) {
                        dialogState.value = DialogParams(
                            title = LearnUppStrings.SET_AS_PRIMARY.getValue(),
                            message = LearnUppStrings.COMING_SOON.getValue(),
                            dialogType = DialogType.INFO,
                            buttonCount = 1,
                            confirmText = LearnUppStrings.OK.getValue(),
                            onConfirm = { dialogState.value = null },
                            onDismiss = { dialogState.value = null }
                        )
                        showSettingsSheet = false
                    }
                    SettingsSheetOption(
                        icon = Icons.Outlined.Notifications,
                        text = LearnUppStrings.NOTIFICATIONS.getValue(),
                        iconBackground = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                    ) {
                        showSettingsSheet = false
                        navigator.safePush(NotificationsSettingsScreen())
                    }
                    SettingsSheetOption(
                        icon = Icons.Outlined.AttachMoney,
                        text = LearnUppStrings.EARNINGS.getValue(),
                        iconBackground = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                    ) {
                        showSettingsSheet = false
                        navigator.safePush(EarningsScreen())
                    }
                    SettingsSheetOption(
                        icon = Icons.Outlined.Payment,
                        text = LearnUppStrings.PAYMENT_METHODS.getValue(),
                        iconBackground = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                    ) {
                        showSettingsSheet = false
                        navigator.safePush(PaymentMethodsScreen())
                    }
                    SettingsSheetOption(
                        icon = Icons.Outlined.Language,
                        text = LearnUppStrings.LANGUAGE.getValue(),
                        iconBackground = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                    ) {
                        showSettingsSheet = false
                        navigator.safePush(LanguageSelectionScreen())
                    }
                    SettingsSheetOption(
                        icon = Icons.Outlined.HelpOutline,
                        text = LearnUppStrings.HELP_SUPPORT.getValue(),
                        iconBackground = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                    ) {
                        showSettingsSheet = false
                        navigator.safePush(HelpSupportScreen())
                    }
                    SettingsSheetOption(
                        icon = Icons.Outlined.Logout,
                        text = LearnUppStrings.LOG_OUT.getValue(),
                        iconTint = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary,
                        iconBackground = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        showChevron = false
                    ) {
                        showSettingsSheet = false
                        dialogState.value = DialogParams(
                            title = LearnUppStrings.LOG_OUT_TITLE.getValue(),
                            message = "",
                            dialogType = DialogType.QUESTION,
                            buttonCount = 2,
                            confirmText = LearnUppStrings.LOG_OUT.getValue(),
                            dismissText = LearnUppStrings.CANCEL.getValue(),
                            onConfirm = {
                                dialogState.value = null
                                moreScreenModel.screenModelScope.launch {
                                    val success = moreScreenModel.logout()
                                    if (success) navigator.replaceAll(com.learnupp.ui.auth.AuthStartScreen())
                                }
                            },
                            onDismiss = { dialogState.value = null }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        ProfileScaffold(
            profile = profile,
            videos = videos,
            reels = reels,
            onLoadMoreVideos = { moreScreenModel.loadMoreForVideos() },
            onLoadMoreReels = { moreScreenModel.loadMoreForReels() },
            onEditAbout = { moreScreenModel.updateProfileInfo(about = it) },
            onSettingsClick = { showSettingsSheet = true }
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
                    GridThumb(url = video.thumbnailUrl)
                }
            }

            ProfileTab.Shorts -> {
                itemsIndexed(reels, key = { _, r -> r.id }) { index, reel ->
                    if (shouldTriggerLoad(index, reels.size)) {
                        LaunchedEffect(reels.size) { onLoadMoreReels() }
                    }
                    GridThumb(url = reel.thumbnailUrl ?: "")
                }
            }

            ProfileTab.Courses -> {
                if (profile.isLecturer) {
                    itemsIndexed(videos, key = { _, v -> "course-${v.id}" }) { _, video ->
                        GridThumb(url = video.thumbnailUrl)
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
                text = profile.fullName ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = profile.title ?: "",
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
            value = formatCount(if (profile.isLecturer) profile.studentsCount else profile.followersCount),
            label = (
                    if (profile.isLecturer) LearnUppStrings.STUDENTS_LABEL else LearnUppStrings.FOLLOWERS_LABEL
                    ).getValue(),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = formatCount(if (profile.isLecturer) profile.coursesCount else profile.followingCount),
            label = (
                    if (profile.isLecturer) LearnUppStrings.COURSES_LABEL else LearnUppStrings.FOLLOWING_LABEL
                    ).getValue(),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = if (profile.isLecturer) formatRating(profile.rating) else formatCount(profile.postsCount),
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


