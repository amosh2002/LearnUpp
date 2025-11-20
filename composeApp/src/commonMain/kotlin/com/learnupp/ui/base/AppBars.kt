package com.learnupp.ui.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.learnupp.ui.messages.MessagesScreen
import com.learnupp.ui.more.MoreScreen
import com.learnupp.ui.reels.ReelsScreen
import com.learnupp.ui.videos.VideosScreen

enum class ScreenNameStrings(val valueEN: String) {
    VIDEOS("Videos"),
    REELS("Reels"),
    MESSAGES("Messages"),
    MORE("More"),
    SETTINGS("Settings"),
    LOGIN("Login");
}

fun ScreenNameStrings.getValue() = valueEN

interface NavigationItem {
    val title: ScreenNameStrings
}

enum class NavBarItems(override val title: ScreenNameStrings) : NavigationItem {
    Videos(ScreenNameStrings.VIDEOS),
    Reels(ScreenNameStrings.REELS),
    Messages(ScreenNameStrings.MESSAGES),
    More(ScreenNameStrings.MORE)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnUppTopAppBar() {
    val navigator = LocalNavigator.currentOrThrow
    val currentScreen = navigator.lastItem as BaseScreen

    if (currentScreen.hideTopAppBar) {
        if (currentScreen.ignoreTopImePadding) {
            Box(modifier = Modifier.fillMaxWidth())
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeContent.only(WindowInsetsSides.Top))
            )
        }
        return
    }

    TopAppBar(
        title = {
            Text(
                text = currentScreen.key,
                color = MaterialTheme.colorScheme.onBackground, // White text
                style = MaterialTheme.typography.titleMedium,
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background, // Dark background
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        navigationIcon = {
            if (navigator.canPop) {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground // White icon
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(9.dp))
            }
        },
    )
}

@Composable
fun LearnUppBottomNavBar() {
    val navigator = LocalNavigator.currentOrThrow
    val currentScreen = navigator.lastItem as BaseScreen

    if (currentScreen.hideBottomNavBar) {
        if (currentScreen.ignoreBottomImePadding) {
            Box(modifier = Modifier.fillMaxWidth())
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeContent.only(WindowInsetsSides.Bottom))
            )
        }
        return
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background, // Dark background
    ) {
        NavBarItems.entries.forEach { item ->
            val isSelected = (navigator.lastItem as? BaseScreen)?.key == item.title.getValue()
            val iconChar = when (item) {
                NavBarItems.Videos -> Icons.Default.VideoFile
                NavBarItems.Reels -> Icons.Default.PlayArrow
                NavBarItems.Messages -> Icons.Default.Chat
                NavBarItems.More -> Icons.Default.MoreHoriz
            }
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = iconChar,
                        contentDescription = item.title.getValue(),
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                },
                label = {
                    Text(
                        item.title.getValue(),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                },
                selected = isSelected,
                enabled = true,
                onClick = {
                    val screen = when (item) {
                        NavBarItems.Videos -> VideosScreen()
                        NavBarItems.Reels -> ReelsScreen()
                        NavBarItems.Messages -> MessagesScreen()
                        NavBarItems.More -> MoreScreen()
                    }
                    if (!isSelected) {
                        navigator.replaceAll(screen)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary, // Red for selected
                    unselectedIconColor = MaterialTheme.colorScheme.secondary, // Light gray for unselected
                    selectedTextColor = MaterialTheme.colorScheme.primary, // Red for selected
                    unselectedTextColor = MaterialTheme.colorScheme.secondary, // Light gray for unselected
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


