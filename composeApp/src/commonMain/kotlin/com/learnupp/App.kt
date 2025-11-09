package com.learnupp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.learnupp.ui.login.LoginScreen
import com.learnupp.ui.base.LearnUppBottomNavBar
import com.learnupp.ui.base.LearnUppTopAppBar
import com.learnupp.ui.base.AppTheme
import com.learnupp.ui.home.HomeScreen
import com.learnupp.util.LearnUppNonPrimaryColors
import com.learnupp.util.LanguageEnum
import com.learnupp.util.LocalizationService
import com.learnupp.util.Logger
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.Platform
import com.learnupp.util.PlatformBackHandler
import com.learnupp.util.PreferencesManager
import com.learnupp.util.SessionManager
import com.learnupp.util.currentPlatform
import com.learnupp.util.getValue
import com.learnupp.ui.widgets.AlertDialogWithBlur
import com.learnupp.ui.widgets.LoadingScreen
import kotlinx.coroutines.runBlocking

private const val TAG = "App.kt"

@Composable
fun App(
    preferencesManager: PreferencesManager,
    localizationService: LocalizationService,
) {
    val loadingState = remember { mutableStateOf(true) }

    val appJustOpened = remember { mutableStateOf(true) }

    // State for the global dialog
    val dialogState = remember { mutableStateOf<DialogParams?>(null) }

    // State for the global toast
    val snackBarHostState = remember { mutableStateOf(SnackbarHostState()) }

    // State for asking before exiting a screen
    val askBeforeGoingBack = remember { mutableStateOf<Boolean?>(false) }

    // State for asking before tapping on navbar items
    val askBeforeMovingToTab = remember { mutableStateOf<Boolean?>(false) }

    // State for clearing all progress in CwList and Services screens
    val clearAllProgress = remember { mutableStateOf(false) }

    // State for checking if new notifications are available
    val newNotificationsState = remember { mutableStateOf(false) }


    // State for the selected language
    val selectedLanguageState = remember {
        // Initialize with the stored language or default to Armenian
        val savedLangIso = runBlocking {
            preferencesManager.getString("LANGUAGE") ?: LanguageEnum.Armenian.iso
        }
        Logger.i(TAG, "Initial language loaded: $savedLangIso")
        mutableStateOf(LanguageEnum.entries.first { it.iso == savedLangIso })
    }

    // Sync language changes to PreferencesManager and LocalizationService
    LaunchedEffect(selectedLanguageState.value) {
        try {
            preferencesManager.saveString("LANGUAGE", selectedLanguageState.value.iso)
            Logger.i(
                TAG,
                "Language saved to PreferencesManager: ${selectedLanguageState.value.iso}"
            )
            localizationService.applyLanguage(selectedLanguageState.value.iso)
            Logger.i(TAG, "Language synced to UserDefaults: ${selectedLanguageState.value.iso}")
        } catch (e: Exception) {
            Logger.e(TAG, "Failed to save or sync language: ${e.message}")
            e.printStackTrace()
        }
    }

    var totalDrag by remember { mutableStateOf(0f) }
    val swipeThreshold = 80f // or whatever distance feels right

    AppTheme {
        CompositionLocalProvider(
            LocalDialogState provides dialogState,
            LocalAskBeforeGoingBack provides askBeforeGoingBack,
            LocalSelectedLanguage provides selectedLanguageState,
            LocalAskBeforeMovingToTab provides askBeforeMovingToTab,
            LocalClearAllProgress provides clearAllProgress,
            LocalSnackBarHostState provides snackBarHostState,
            LocalUnreadNotificationsStatus provides newNotificationsState,
            LocalAppJustOpened provides appJustOpened,
        ) { // Provide the dialog and back-confirmation state
            LaunchedEffect(Unit) {
                // Initialize the session manager to load user roles
                SessionManager.initialize(preferencesManager)

                // Done loading
                loadingState.value = false
            }

            if (loadingState.value) {
                LoadingScreen()
            } else {
                // Decide the start screen based on login status
                Logger.i(TAG, "User logged in: ${SessionManager.isLoggedIn()}")
                val startScreen = if (SessionManager.isLoggedIn()) {
                    HomeScreen()
                } else {
                    LoginScreen()
                }

                Navigator(startScreen) { navigator ->
                    Box(
                        Modifier.fillMaxSize() // Full-screen container
                    ) {
                        // Add BackHandler here to intercept Android back events
                        PlatformBackHandler(enabled = navigator.canPop) {
                            navigator.safePop(
                                askBeforeGoingBack,
                                askBeforeMovingToTab,
                                clearAllProgress,
                                dialogState
                            )
                        }

                        // Main app structure
                        Scaffold(
                            topBar = { LearnUppTopAppBar() },
                            bottomBar = { LearnUppBottomNavBar() },
                            snackbarHost = {
                                SnackbarHost(
                                    hostState = LocalSnackBarHostState.current.value,
                                    snackbar = { snackBarData ->
                                        Snackbar(
                                            containerColor = LearnUppNonPrimaryColors.VERY_LIGHT_GRAY,
                                            contentColor = MaterialTheme.colorScheme.onPrimary,
                                            shape = RoundedCornerShape(7.dp),
                                            modifier = Modifier.wrapContentWidth()
                                        ) {
                                            Text(
                                                modifier = Modifier
                                                    .padding(horizontal = 8.dp),
                                                text = snackBarData.visuals.message,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                textAlign = TextAlign.Center,
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .wrapContentWidth()
                                )
                            }
                        ) { innerPadding ->
                            SlideTransition(
                                navigator = navigator,
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .consumeWindowInsets(innerPadding)
                            )
                        }

                        // ===== iOS Back-swipe region =====
                        if (currentPlatform == Platform.iOS) {
                            Box(
                                modifier = Modifier
                                    .width(22.dp)
                                    .fillMaxHeight()
                                    .pointerInput(Unit) {
                                        detectHorizontalDragGestures(
                                            onDragStart = {
                                                // Reset a total drag accumulator whenever the finger first touches
                                                totalDrag = 0f
                                            },
                                            onHorizontalDrag = { _, dragAmount ->
                                                // Accumulate only positive drags (i.e., left-to-right)
                                                if (dragAmount > 0) {
                                                    totalDrag += dragAmount
                                                }
                                            },
                                            onDragEnd = {
                                                // Finger lifted: if total drag crosses threshold, pop once
                                                if (totalDrag > swipeThreshold && navigator.canPop
                                                ) {
                                                    navigator.safePop(
                                                        askBeforeGoingBack,
                                                        askBeforeMovingToTab,
                                                        clearAllProgress,
                                                        dialogState
                                                    )
                                                }
                                            },
                                            onDragCancel = {
                                                // Optional: you could reset totalDrag or do other cleanup here
                                                totalDrag = 0f
                                            }
                                        )
                                    }
                            )
                        }

                        // Full-screen blur and dialog overlay
                        val focusManager = LocalFocusManager.current
                        val softwareKeyboardController = LocalSoftwareKeyboardController.current

                        dialogState.value?.let { params ->

                            // Clear focus when the dialog appears.
                            LaunchedEffect(params) {
                                focusManager.clearFocus()
                                softwareKeyboardController?.hide()
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = 0.7f
                                        )
                                    )
                                    .clickable(
                                        onClick = {},
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) // Blocks clicks
                            )

                            AlertDialogWithBlur(
                                isVisible = true,
                                title = params.title,
                                message = params.message,
                                annotatedMessage = params.annotatedMessage,
                                dialogType = params.dialogType,
                                onConfirm = {
                                    params.onConfirm?.invoke()
                                    dialogState.value = null
                                },
                                onDismiss = {
                                    params.onDismiss?.invoke()
                                    dialogState.value = null
                                },
                                confirmText = params.confirmText,
                                dismissText = params.dismissText,
                                buttonCount = params.buttonCount,
                                isConfirmPositive = params.isConfirmPositive,
                            )
                        }
                    }

                    /* listen once for every logout and kill the back-stack */
                    LaunchedEffect(Unit) {
                        SessionManager.logoutEvents.collect {
                            if (navigator.lastItem !is LoginScreen) {
                                dialogState.value = DialogParams(
                                    title = LearnUppStrings.FAILED_TO_AUTHENTICATE.getValue(),
                                    message = LearnUppStrings.PLEASE_LOG_IN_AGAIN.getValue(),
                                    dialogType = DialogType.ERROR,
                                    buttonCount = 1,
                                    confirmText = LearnUppStrings.OK.getValue(),
                                    onConfirm = {
                                        dialogState.value = null
                                        if (navigator.items.contains(LoginScreen())) {
                                            navigator.popUntil { it == LoginScreen() }
                                        } else {
                                            navigator.replaceAll(LoginScreen())
                                        }
                                    },
                                    onDismiss = {
                                        dialogState.value = null
                                        if (navigator.items.contains(LoginScreen())) {
                                            navigator.popUntil { it == LoginScreen() }
                                        } else {
                                            navigator.replaceAll(LoginScreen())
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}


// Define a CompositionLocal to know if the app is opened first time after kill
val LocalAppJustOpened = staticCompositionLocalOf<MutableState<Boolean>> {
    error("LocalAppJustOpened not provided. Did you forget to wrap your content in CompositionLocalProvider?")
}

// Define a CompositionLocal to keep the status of new notifications
val LocalUnreadNotificationsStatus = staticCompositionLocalOf<MutableState<Boolean>> {
    error("LocalUnreadNotificationsStatus not provided. Did you forget to wrap your content in CompositionLocalProvider?")
}

// Define a CompositionLocal to show a toast message
val LocalSnackBarHostState = staticCompositionLocalOf<MutableState<SnackbarHostState>> {
    error("LocalToastMessage not provided. Did you forget to wrap your content in CompositionLocalProvider?")
}

// Define a CompositionLocal for the dialog state
val LocalDialogState = staticCompositionLocalOf<MutableState<DialogParams?>> {
    error("LocalDialogState not provided. Did you forget to wrap your content in CompositionLocalProvider?")
}

// Define a CompositionLocal for the selected language
val LocalSelectedLanguage = staticCompositionLocalOf<MutableState<LanguageEnum>> {
    error("LocalSelectedLanguage not provided. Did you forget to wrap your content in CompositionLocalProvider?")
}

// Define a CompositionLocal for clearing all process when leaving a screen
val LocalClearAllProgress = staticCompositionLocalOf<MutableState<Boolean>> {
    error("LocalClearAllProgress not provided. Did you forget to wrap your content in CompositionLocalProvider?")
}

// Define a CompositionLocal for the confirmation when tapped on navbar items
val LocalAskBeforeMovingToTab = staticCompositionLocalOf<MutableState<Boolean?>> {
    error("LocalAskBeforeMovingToTab not provided. Did you forget to wrap your content in CompositionLocalProvider?")
}

// Define a CompositionLocal for the confirmation before going back
val LocalAskBeforeGoingBack = staticCompositionLocalOf<MutableState<Boolean?>> {
    error("LocalAskBeforeGoingBack not provided. Did you forget to wrap your content in CompositionLocalProvider?")
}


// Define a data class to hold dialog parameters
data class DialogParams(
    val title: String,
    val message: String? = null,
    val annotatedMessage: AnnotatedString? = null,
    val dialogType: DialogType = DialogType.ERROR,
    val onConfirm: (() -> Unit)? = null,
    val onDismiss: (() -> Unit)? = null,
    val confirmText: String = LearnUppStrings.CONFIRM.getValue(),
    val dismissText: String = LearnUppStrings.CANCEL.getValue(),
    val buttonCount: Int = 1,
    val isConfirmPositive: Boolean = false
)

enum class DialogType {
    SUCCESS,
    ERROR,
    QUESTION,
    INFO
}

fun Navigator.safePop(
    askBeforeGoingBack: MutableState<Boolean?>,
    askBeforeMovingToTab: MutableState<Boolean?>,
    clearAllProgress: MutableState<Boolean>,
    dialogState: MutableState<DialogParams?>
) {
    if (askBeforeGoingBack.value == true) {
        dialogState.value =
            DialogParams(
                title = LearnUppStrings.LEAVE_TITLE.getValue(),
                message = LearnUppStrings.LEAVE_TEXT.getValue(),
                dialogType = DialogType.QUESTION,
                buttonCount = 2,
                confirmText = LearnUppStrings.LEAVE.getValue(),
                dismissText = LearnUppStrings.GO_BACK.getValue(),
                onConfirm = {
                    clearAllProgress.value = true
                    dialogState.value = null
                    val currentScreen = this.lastItem
                    this.pop()
                    askBeforeGoingBack.value = false
                    askBeforeMovingToTab.value = false
                },
                onDismiss = { dialogState.value = null },
            )
    } else {
        if (dialogState.value == null) {
            this.pop()
        }
    }
}
