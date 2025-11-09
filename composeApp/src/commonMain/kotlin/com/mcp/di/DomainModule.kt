package com.mcp.di

import com.mcp.domain.usecase.auth.LoginUseCase
import com.mcp.domain.usecase.auth.LoginUseCaseImpl
import com.mcp.domain.usecase.auth.LogoutUseCase
import com.mcp.domain.usecase.auth.LogoutUseCaseImpl
import com.mcp.domain.usecase.camera.GetAllCamerasUseCase
import com.mcp.domain.usecase.camera.GetCarwashesUseCaseImpl
import com.mcp.domain.usecase.camera.GetFeaturedCameraUseCase
import com.mcp.domain.usecase.camera.GetFeaturedCameraUseCaseImpl
import com.mcp.domain.usecase.camera.PreloadAllCamerasUseCase
import com.mcp.domain.usecase.camera.PreloadAllCamerasUseCaseImpl
import com.mcp.domain.usecase.camera.PreloadFeaturedCameraUseCase
import com.mcp.domain.usecase.camera.PreloadFeaturedCameraUseCaseImpl
import com.mcp.domain.usecase.camera.ReloadAllCamerasUseCase
import com.mcp.domain.usecase.camera.ReloadAllCamerasUseCaseImpl
import com.mcp.domain.usecase.camera.ReloadFeaturedCameraUseCase
import com.mcp.domain.usecase.camera.ReloadFeaturedCameraUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    // Auth
    single<LoginUseCase> { LoginUseCaseImpl(get()) }
    single<LogoutUseCase> { LogoutUseCaseImpl(get()) }

    // Camera
    single<GetAllCamerasUseCase> { GetCarwashesUseCaseImpl(get()) }
    single<GetFeaturedCameraUseCase> { GetFeaturedCameraUseCaseImpl(get()) }
    single<PreloadAllCamerasUseCase> { PreloadAllCamerasUseCaseImpl(get()) }
    single<PreloadFeaturedCameraUseCase> { PreloadFeaturedCameraUseCaseImpl(get()) }
    single<ReloadAllCamerasUseCase> { ReloadAllCamerasUseCaseImpl(get()) }
    single<ReloadFeaturedCameraUseCase> { ReloadFeaturedCameraUseCaseImpl(get()) }
}


