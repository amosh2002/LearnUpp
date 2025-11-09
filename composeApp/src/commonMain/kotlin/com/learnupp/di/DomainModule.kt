package com.learnupp.di

import com.learnupp.domain.usecase.auth.LoginUseCase
import com.learnupp.domain.usecase.auth.LoginUseCaseImpl
import com.learnupp.domain.usecase.auth.LogoutUseCase
import com.learnupp.domain.usecase.auth.LogoutUseCaseImpl
import com.learnupp.domain.usecase.camera.GetAllCamerasUseCase
import com.learnupp.domain.usecase.camera.GetCarwashesUseCaseImpl
import com.learnupp.domain.usecase.camera.GetFeaturedCameraUseCase
import com.learnupp.domain.usecase.camera.GetFeaturedCameraUseCaseImpl
import com.learnupp.domain.usecase.camera.PreloadAllCamerasUseCase
import com.learnupp.domain.usecase.camera.PreloadAllCamerasUseCaseImpl
import com.learnupp.domain.usecase.camera.PreloadFeaturedCameraUseCase
import com.learnupp.domain.usecase.camera.PreloadFeaturedCameraUseCaseImpl
import com.learnupp.domain.usecase.camera.ReloadAllCamerasUseCase
import com.learnupp.domain.usecase.camera.ReloadAllCamerasUseCaseImpl
import com.learnupp.domain.usecase.camera.ReloadFeaturedCameraUseCase
import com.learnupp.domain.usecase.camera.ReloadFeaturedCameraUseCaseImpl
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


