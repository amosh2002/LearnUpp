package com.mcp.ui.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Videocam
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
import com.mcp.ui.cameras.CamerasScreen
import com.mcp.ui.home.HomeScreen
import com.mcp.ui.more.MoreScreen

enum class ScreenNameStrings(val valueEN: String) {
    HOME("Home"),
    CAMERAS("Cameras"),
    MORE("More"),
    LOGIN("Login");
}

fun ScreenNameStrings.getValue() = valueEN

interface NavigationItem {
    val title: ScreenNameStrings
}

enum class NavBarItems(override val title: ScreenNameStrings) : NavigationItem {
    Home(ScreenNameStrings.HOME),
    Cameras(ScreenNameStrings.CAMERAS),
    More(ScreenNameStrings.MORE)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun McpTopAppBar() {
    val navigator = LocalNavigator.currentOrThrow
    val currentScreen = navigator.lastItem

    if (currentScreen.key == ScreenNameStrings.LOGIN.getValue()) {
        Box(modifier = Modifier.fillMaxWidth())
    } else {
        TopAppBar(
            title = {
                Text(
                    text = currentScreen.key,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            navigationIcon = {
                if (navigator.canPop) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(9.dp))
                }
            }
        )
    }
}

@Composable
fun McpBottomNavBar() {
    val navigator = LocalNavigator.currentOrThrow
    val items = NavBarItems.entries

    // Hide bottom bar on Login
    if (navigator.lastItem.key == ScreenNameStrings.LOGIN.getValue()) {
        Box(modifier = Modifier.fillMaxWidth())
        return
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        items.forEach { item ->
            val isSelected = (navigator.lastItem as? BaseScreen)?.key == item.title.getValue()
            val iconChar = when (item) {
                NavBarItems.Home -> Icons.Default.Home
                NavBarItems.Cameras -> Icons.Default.Videocam
                NavBarItems.More -> Icons.Default.MoreHoriz
            }
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = iconChar,
                        contentDescription = item.title.getValue(),
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    Text(
                        item.title.getValue(),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                selected = isSelected,
                enabled = true,
                onClick = {
                    val screen = when (item) {
                        NavBarItems.Home -> HomeScreen()
                        NavBarItems.Cameras -> CamerasScreen()
                        NavBarItems.More -> MoreScreen()
                    }
                    if (!isSelected) {
                        navigator.replaceAll(screen)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


