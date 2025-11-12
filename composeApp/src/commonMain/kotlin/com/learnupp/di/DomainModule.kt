package com.learnupp.di

import com.learnupp.domain.usecase.auth.LoginUseCase
import com.learnupp.domain.usecase.auth.LoginUseCaseImpl
import com.learnupp.domain.usecase.auth.LogoutUseCase
import com.learnupp.domain.usecase.auth.LogoutUseCaseImpl
import com.learnupp.domain.usecase.reels.GetLikedReelsUseCase
import com.learnupp.domain.usecase.reels.GetLikedReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.GetReelsUseCase
import com.learnupp.domain.usecase.reels.GetReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.LoadMoreReelsUseCase
import com.learnupp.domain.usecase.reels.LoadMoreReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.PreloadReelsUseCase
import com.learnupp.domain.usecase.reels.PreloadReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.ReloadReelsUseCase
import com.learnupp.domain.usecase.reels.ReloadReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.ShareReelUseCase
import com.learnupp.domain.usecase.reels.ShareReelUseCaseImpl
import com.learnupp.domain.usecase.reels.ToggleReelLikeUseCase
import com.learnupp.domain.usecase.reels.ToggleReelLikeUseCaseImpl
import com.learnupp.domain.usecase.videos.GetLikedVideosUseCase
import com.learnupp.domain.usecase.videos.GetLikedVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.GetVideosUseCase
import com.learnupp.domain.usecase.videos.GetVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.LoadMoreVideosUseCase
import com.learnupp.domain.usecase.videos.LoadMoreVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.PreloadVideosUseCase
import com.learnupp.domain.usecase.videos.PreloadVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.ReloadVideosUseCase
import com.learnupp.domain.usecase.videos.ReloadVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.ShareVideoUseCase
import com.learnupp.domain.usecase.videos.ShareVideoUseCaseImpl
import com.learnupp.domain.usecase.videos.ToggleVideoLikeUseCase
import com.learnupp.domain.usecase.videos.ToggleVideoLikeUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    // Auth
    single<LoginUseCase> { LoginUseCaseImpl(get()) }
    single<LogoutUseCase> { LogoutUseCaseImpl(get()) }

    // Reels
    single<GetReelsUseCase> { GetReelsUseCaseImpl(get()) }
    single<GetLikedReelsUseCase> { GetLikedReelsUseCaseImpl(get()) }
    single<PreloadReelsUseCase> { PreloadReelsUseCaseImpl(get()) }
    single<ReloadReelsUseCase> { ReloadReelsUseCaseImpl(get()) }
    single<ToggleReelLikeUseCase> { ToggleReelLikeUseCaseImpl(get()) }
    single<LoadMoreReelsUseCase> { LoadMoreReelsUseCaseImpl(get()) }
    single<ShareReelUseCase> { ShareReelUseCaseImpl(get()) }

    // Videos
    single<GetVideosUseCase> { GetVideosUseCaseImpl(get()) }
    single<GetLikedVideosUseCase> { GetLikedVideosUseCaseImpl(get()) }
    single<PreloadVideosUseCase> { PreloadVideosUseCaseImpl(get()) }
    single<ReloadVideosUseCase> { ReloadVideosUseCaseImpl(get()) }
    single<ToggleVideoLikeUseCase> { ToggleVideoLikeUseCaseImpl(get()) }
    single<LoadMoreVideosUseCase> { LoadMoreVideosUseCaseImpl(get()) }
    single<ShareVideoUseCase> { ShareVideoUseCaseImpl(get()) }
}


