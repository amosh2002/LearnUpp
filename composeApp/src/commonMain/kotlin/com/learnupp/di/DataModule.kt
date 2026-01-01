package com.learnupp.di

import com.learnupp.data.auth.AuthApi
import com.learnupp.data.auth.AuthRepositoryImpl
import com.learnupp.data.auth.KtorAuthApi
import com.learnupp.data.courses.CoursesApi
import com.learnupp.data.courses.CoursesRepositoryImpl
import com.learnupp.data.courses.CoursesStorage
import com.learnupp.data.courses.InMemoryCoursesStorage
import com.learnupp.data.courses.KtorCoursesApi
import com.learnupp.data.earnings.EarningsApi
import com.learnupp.data.earnings.EarningsRepositoryImpl
import com.learnupp.data.earnings.EarningsStorage
import com.learnupp.data.earnings.InMemoryEarningsStorage
import com.learnupp.data.earnings.MockEarningsApi
import com.learnupp.data.messages.InMemoryMessagesStorage
import com.learnupp.data.messages.MessagesApi
import com.learnupp.data.messages.MessagesRepositoryImpl
import com.learnupp.data.messages.MessagesStorage
import com.learnupp.data.messages.MockMessagesApi
import com.learnupp.data.payments.InMemoryPaymentMethodsStorage
import com.learnupp.data.payments.MockPaymentMethodsApi
import com.learnupp.data.payments.PaymentMethodsApi
import com.learnupp.data.payments.PaymentMethodsRepositoryImpl
import com.learnupp.data.payments.PaymentMethodsStorage
import com.learnupp.data.settings.language.InMemoryLanguageOptionsStorage
import com.learnupp.data.settings.language.LanguageOptionsApi
import com.learnupp.data.settings.language.LanguageOptionsRepositoryImpl
import com.learnupp.data.settings.language.LanguageOptionsStorage
import com.learnupp.data.settings.language.MockLanguageOptionsApi
import com.learnupp.data.settings.notifications.InMemoryNotificationsStorage
import com.learnupp.data.settings.notifications.MockNotificationsApi
import com.learnupp.data.settings.notifications.NotificationsApi
import com.learnupp.data.settings.notifications.NotificationsRepositoryImpl
import com.learnupp.data.settings.notifications.NotificationsStorage
import com.learnupp.data.profile.InMemoryProfileStorage
import com.learnupp.data.profile.KtorProfileApi
import com.learnupp.data.profile.ProfileApi
import com.learnupp.data.profile.ProfileRepositoryImpl
import com.learnupp.data.profile.ProfileStorage
import com.learnupp.data.reels.InMemoryReelsStorage
import com.learnupp.data.reels.KtorReelsApi
import com.learnupp.data.reels.ReelsApi
import com.learnupp.data.reels.ReelsRepositoryImpl
import com.learnupp.data.reels.ReelsStorage
import com.learnupp.data.videos.InMemoryVideosStorage
import com.learnupp.data.videos.KtorVideosApi
import com.learnupp.data.videos.VideosApi
import com.learnupp.data.videos.VideosRepositoryImpl
import com.learnupp.data.videos.VideosStorage
import com.learnupp.domain.repo.AuthRepository
import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.repo.EarningsRepository
import com.learnupp.domain.repo.MessagesRepository
import com.learnupp.domain.repo.ProfileRepository
import com.learnupp.domain.repo.LanguageOptionsRepository
import com.learnupp.domain.repo.NotificationsRepository
import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.repo.PaymentMethodsRepository
import org.koin.dsl.module

val dataModule = module {
    // Auth
    single<AuthApi> { KtorAuthApi(client = get(Qualifiers.Auth)) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // Profile (real)
    single<ProfileApi> { KtorProfileApi(client = get(Qualifiers.Plain)) }
    single<ProfileStorage> { InMemoryProfileStorage() }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }

    // Messages (mocked)
    single<MessagesApi> { MockMessagesApi() }
    single<MessagesStorage> { InMemoryMessagesStorage() }
    single<MessagesRepository> { MessagesRepositoryImpl(get(), get()) }

    // Courses (real)
    single<CoursesApi> { KtorCoursesApi(client = get(Qualifiers.Plain)) }
    single<CoursesStorage> { InMemoryCoursesStorage() }
    single<CoursesRepository> { CoursesRepositoryImpl(get(), get()) }

    // Notifications
    single<NotificationsApi> { MockNotificationsApi() }
    single<NotificationsStorage> { InMemoryNotificationsStorage() }
    single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }

    // Earnings
    single<EarningsApi> { MockEarningsApi() }
    single<EarningsStorage> { InMemoryEarningsStorage() }
    single<EarningsRepository> { EarningsRepositoryImpl(get(), get()) }

    // Payment methods
    single<PaymentMethodsApi> { MockPaymentMethodsApi() }
    single<PaymentMethodsStorage> { InMemoryPaymentMethodsStorage() }
    single<PaymentMethodsRepository> { PaymentMethodsRepositoryImpl(get(), get()) }

    // Language options
    single<LanguageOptionsApi> { MockLanguageOptionsApi() }
    single<LanguageOptionsStorage> { InMemoryLanguageOptionsStorage() }
    single<LanguageOptionsRepository> { LanguageOptionsRepositoryImpl(get(), get(), get()) }

    // Reels (real)
    single<ReelsApi> { KtorReelsApi(client = get(Qualifiers.Plain)) }
    single<ReelsStorage> { InMemoryReelsStorage() }
    single<ReelsRepository> { ReelsRepositoryImpl(get(), get()) }

    // Videos (real)
    single<VideosApi> { KtorVideosApi(client = get(Qualifiers.Plain)) }
    single<VideosStorage> { InMemoryVideosStorage() }
    single<VideosRepository> { VideosRepositoryImpl(get(), get()) }
}


