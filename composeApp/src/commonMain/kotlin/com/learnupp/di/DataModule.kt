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
import org.koin.dsl.module

val dataModule = module {
    // Auth
    single<AuthApi> { KtorAuthApi(client = get(Qualifiers.Plain)) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // Camera
    single<CameraApi> { KtorCameraApi(client = get(Qualifiers.Auth)) }
    single<CameraStorage> { InMemoryCameraStorage() }
    single<CameraRepository> { CameraRepositoryImpl(get(), get()) }
}


