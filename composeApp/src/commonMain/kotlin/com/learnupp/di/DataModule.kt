package com.learnupp.di

import com.learnupp.data.auth.AuthApi
import com.learnupp.data.auth.AuthRepositoryImpl
import com.learnupp.data.auth.KtorAuthApi
import com.learnupp.data.camera.CameraApi
import com.learnupp.data.camera.CameraRepositoryImpl
import com.learnupp.data.camera.CameraStorage
import com.learnupp.data.camera.InMemoryCameraStorage
import com.learnupp.data.camera.KtorCameraApi
import com.learnupp.domain.repo.AuthRepository
import com.learnupp.domain.repo.CameraRepository
import com.learnupp.data.reels.ReelsApi
import com.learnupp.data.reels.MockReelsApi
import com.learnupp.data.reels.ReelsStorage
import com.learnupp.data.reels.InMemoryReelsStorage
import com.learnupp.data.reels.ReelsRepositoryImpl
import com.learnupp.domain.repo.ReelsRepository
import org.koin.dsl.module

val dataModule = module {
    // Auth
    single<AuthApi> { KtorAuthApi(client = get(Qualifiers.Plain)) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // Camera
    single<CameraApi> { KtorCameraApi(client = get(Qualifiers.Auth)) }
    single<CameraStorage> { InMemoryCameraStorage() }
    single<CameraRepository> { CameraRepositoryImpl(get(), get()) }

    // Reels (mocked)
    single<ReelsApi> { MockReelsApi() }
    single<ReelsStorage> { InMemoryReelsStorage() }
    single<ReelsRepository> { ReelsRepositoryImpl(get(), get()) }
}


