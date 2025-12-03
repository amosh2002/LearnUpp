package com.learnupp.di

import com.learnupp.ui.courses.CoursesScreenModel
import com.learnupp.ui.login.LoginScreenModel
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
    // Login
    single {
        LoginScreenModel(
            get()
        )
    }

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
            // Profile
            get(),
            get(),
            get(),
            get(),
            // Videos
            get(),
            get(),
            get(),
            get(),
            // Reels
            get(),
            get(),
            get(),
            get()
        )
    }

    // Settings
    single { NotificationsSettingsScreenModel(get(), get(), get()) }
    single { EarningsScreenModel(get(), get()) }
    single { PaymentMethodsScreenModel(get(), get()) }
    single { LanguageSelectionScreenModel(get(), get(), get()) }
}


