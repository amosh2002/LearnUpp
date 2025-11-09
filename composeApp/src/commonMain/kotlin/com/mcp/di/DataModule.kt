package com.mcp.di

import com.mcp.data.auth.AuthApi
import com.mcp.data.auth.AuthRepositoryImpl
import com.mcp.data.auth.KtorAuthApi
import com.mcp.data.camera.CameraApi
import com.mcp.data.camera.CameraRepositoryImpl
import com.mcp.data.camera.CameraStorage
import com.mcp.data.camera.InMemoryCameraStorage
import com.mcp.data.camera.KtorCameraApi
import com.mcp.domain.repo.AuthRepository
import com.mcp.domain.repo.CameraRepository
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


