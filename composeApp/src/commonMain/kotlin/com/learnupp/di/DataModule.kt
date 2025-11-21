package com.learnupp.di

import com.learnupp.data.auth.AuthApi
import com.learnupp.data.auth.AuthRepositoryImpl
import com.learnupp.data.auth.KtorAuthApi
import com.learnupp.data.courses.CoursesApi
import com.learnupp.data.courses.CoursesRepositoryImpl
import com.learnupp.data.courses.CoursesStorage
import com.learnupp.data.courses.InMemoryCoursesStorage
import com.learnupp.data.courses.MockCoursesApi
import com.learnupp.data.messages.InMemoryMessagesStorage
import com.learnupp.data.messages.MessagesApi
import com.learnupp.data.messages.MessagesRepositoryImpl
import com.learnupp.data.messages.MessagesStorage
import com.learnupp.data.messages.MockMessagesApi
import com.learnupp.data.profile.InMemoryProfileStorage
import com.learnupp.data.profile.MockProfileApi
import com.learnupp.data.profile.ProfileApi
import com.learnupp.data.profile.ProfileRepositoryImpl
import com.learnupp.data.profile.ProfileStorage
import com.learnupp.data.reels.InMemoryReelsStorage
import com.learnupp.data.reels.MockReelsApi
import com.learnupp.data.reels.ReelsApi
import com.learnupp.data.reels.ReelsRepositoryImpl
import com.learnupp.data.reels.ReelsStorage
import com.learnupp.data.videos.InMemoryVideosStorage
import com.learnupp.data.videos.MockVideosApi
import com.learnupp.data.videos.VideosApi
import com.learnupp.data.videos.VideosRepositoryImpl
import com.learnupp.data.videos.VideosStorage
import com.learnupp.domain.repo.AuthRepository
import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.repo.MessagesRepository
import com.learnupp.domain.repo.ProfileRepository
import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.repo.VideosRepository
import org.koin.dsl.module

val dataModule = module {
    // Auth
    single<AuthApi> { KtorAuthApi(client = get(Qualifiers.Plain)) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // Profile (mocked)
    single<ProfileApi> { MockProfileApi() }
    single<ProfileStorage> { InMemoryProfileStorage() }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }

    // Messages (mocked)
    single<MessagesApi> { MockMessagesApi() }
    single<MessagesStorage> { InMemoryMessagesStorage() }
    single<MessagesRepository> { MessagesRepositoryImpl(get(), get()) }

    // Courses (mocked)
    single<CoursesApi> { MockCoursesApi() }
    single<CoursesStorage> { InMemoryCoursesStorage() }
    single<CoursesRepository> { CoursesRepositoryImpl(get(), get()) }

    // Reels (mocked)
    single<ReelsApi> { MockReelsApi() }
    single<ReelsStorage> { InMemoryReelsStorage() }
    single<ReelsRepository> { ReelsRepositoryImpl(get(), get()) }

    // Videos (mocked)
    single<VideosApi> { MockVideosApi() }
    single<VideosStorage> { InMemoryVideosStorage() }
    single<VideosRepository> { VideosRepositoryImpl(get(), get()) }
}


