package com.learnupp.di

import com.learnupp.ui.auth.AuthStartScreenModel
import com.learnupp.ui.auth.OtpScreenModel
import com.learnupp.ui.auth.ProfileCompletionScreenModel
import com.learnupp.ui.courses.CoursesScreenModel
import com.learnupp.ui.messages.MessagesScreenModel
import com.learnupp.ui.more.MoreScreenModel
import com.learnupp.ui.reels.ReelsScreenModel
import com.learnupp.ui.settings.earnings.EarningsScreenModel
import com.learnupp.ui.settings.language.LanguageSelectionScreenModel
import com.learnupp.ui.settings.notifications.NotificationsSettingsScreenModel
import com.learnupp.ui.settings.payments.PaymentMethodsScreenModel
import com.learnupp.ui.videos.VideosScreenModel
import org.koin.dsl.module

val screenModelsModule = module {
    // Auth
    single { AuthStartScreenModel(get(), get()) }
    single { OtpScreenModel(get(), get()) }
    single { ProfileCompletionScreenModel(get(), get()) }

    // Reels
    single {
        ReelsScreenModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    // Videos
    single {
        VideosScreenModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    // Courses
    single {
        CoursesScreenModel(
            get(),
            get(),
            get(),
            get()
        )
    }

    // Messages
    single {
        MessagesScreenModel(
            get(),
            get(),
            get()
        )
    }

    // More
    single {
        MoreScreenModel(
            get(), // Logout
            get(),
            get(),
            get(),
            get(), // Profile
            get(),
            get(),
            get(),
            get(), // Videos (Preload, RefreshMyProfile, GetMyProfile, LoadMoreMyProfile)
            get(),
            get(),
            get(),
            get(), get(),
            get(),
            get(),
            get(),
            get(),  // Reels (Preload, RefreshMyProfile, GetMyProfile, LoadMoreMyProfile)
        )
    }

    // Settings
    single { NotificationsSettingsScreenModel(get(), get(), get()) }
    single { EarningsScreenModel(get(), get()) }
    single { PaymentMethodsScreenModel(get(), get()) }
    single { LanguageSelectionScreenModel(get(), get(), get()) }
}


