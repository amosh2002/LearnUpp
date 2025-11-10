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
import com.learnupp.domain.usecase.reels.GetReelsUseCase
import com.learnupp.domain.usecase.reels.GetReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.PreloadReelsUseCase
import com.learnupp.domain.usecase.reels.PreloadReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.ReloadReelsUseCase
import com.learnupp.domain.usecase.reels.ReloadReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.ToggleReelLikeUseCase
import com.learnupp.domain.usecase.reels.ToggleReelLikeUseCaseImpl
import com.learnupp.domain.usecase.reels.LoadMoreReelsUseCase
import com.learnupp.domain.usecase.reels.LoadMoreReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.GetLikedReelsUseCase
import com.learnupp.domain.usecase.reels.GetLikedReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.ShareReelUseCase
import com.learnupp.domain.usecase.reels.ShareReelUseCaseImpl
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

    // Reels
    single<GetReelsUseCase> { GetReelsUseCaseImpl(get()) }
    single<GetLikedReelsUseCase> { GetLikedReelsUseCaseImpl(get()) }
    single<PreloadReelsUseCase> { PreloadReelsUseCaseImpl(get()) }
    single<ReloadReelsUseCase> { ReloadReelsUseCaseImpl(get()) }
    single<ToggleReelLikeUseCase> { ToggleReelLikeUseCaseImpl(get()) }
    single<LoadMoreReelsUseCase> { LoadMoreReelsUseCaseImpl(get()) }
    single<ShareReelUseCase> { ShareReelUseCaseImpl(get()) }
}


