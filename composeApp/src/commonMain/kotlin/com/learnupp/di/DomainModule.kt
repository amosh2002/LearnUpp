package com.learnupp.di

import com.learnupp.domain.usecase.auth.LogoutUseCase
import com.learnupp.domain.usecase.auth.LogoutUseCaseImpl
import com.learnupp.domain.usecase.auth.RequestOtpUseCase
import com.learnupp.domain.usecase.auth.RequestOtpUseCaseImpl
import com.learnupp.domain.usecase.auth.VerifyOtpUseCase
import com.learnupp.domain.usecase.auth.VerifyOtpUseCaseImpl
import com.learnupp.domain.usecase.auth.CompleteProfileUseCase
import com.learnupp.domain.usecase.auth.CompleteProfileUseCaseImpl
import com.learnupp.domain.usecase.auth.CheckUsernameUseCase
import com.learnupp.domain.usecase.auth.CheckUsernameUseCaseImpl
import com.learnupp.domain.usecase.auth.LoginWithProviderUseCase
import com.learnupp.domain.usecase.auth.LoginWithProviderUseCaseImpl
import com.learnupp.domain.usecase.messages.GetMessagesUseCase
import com.learnupp.domain.usecase.messages.GetMessagesUseCaseImpl
import com.learnupp.domain.usecase.messages.PreloadMessagesUseCase
import com.learnupp.domain.usecase.messages.PreloadMessagesUseCaseImpl
import com.learnupp.domain.usecase.messages.ReloadMessagesUseCase
import com.learnupp.domain.usecase.messages.ReloadMessagesUseCaseImpl
import com.learnupp.domain.usecase.courses.GetCoursesUseCase
import com.learnupp.domain.usecase.courses.GetCoursesUseCaseImpl
import com.learnupp.domain.usecase.courses.LoadMoreCoursesUseCase
import com.learnupp.domain.usecase.courses.LoadMoreCoursesUseCaseImpl
import com.learnupp.domain.usecase.courses.PreloadCoursesUseCase
import com.learnupp.domain.usecase.courses.PreloadCoursesUseCaseImpl
import com.learnupp.domain.usecase.courses.ReloadCoursesUseCase
import com.learnupp.domain.usecase.courses.ReloadCoursesUseCaseImpl
import com.learnupp.domain.usecase.earnings.GetEarningsSummaryUseCase
import com.learnupp.domain.usecase.earnings.GetEarningsSummaryUseCaseImpl
import com.learnupp.domain.usecase.earnings.ReloadEarningsUseCase
import com.learnupp.domain.usecase.earnings.ReloadEarningsUseCaseImpl
import com.learnupp.domain.usecase.language.GetLanguagesUseCase
import com.learnupp.domain.usecase.language.GetLanguagesUseCaseImpl
import com.learnupp.domain.usecase.language.ReloadLanguagesUseCase
import com.learnupp.domain.usecase.language.ReloadLanguagesUseCaseImpl
import com.learnupp.domain.usecase.language.SelectLanguageUseCase
import com.learnupp.domain.usecase.language.SelectLanguageUseCaseImpl
import com.learnupp.domain.usecase.notifications.GetNotificationSettingsUseCase
import com.learnupp.domain.usecase.notifications.GetNotificationSettingsUseCaseImpl
import com.learnupp.domain.usecase.notifications.ReloadNotificationSettingsUseCase
import com.learnupp.domain.usecase.notifications.ReloadNotificationSettingsUseCaseImpl
import com.learnupp.domain.usecase.notifications.ToggleNotificationSettingUseCase
import com.learnupp.domain.usecase.notifications.ToggleNotificationSettingUseCaseImpl
import com.learnupp.domain.usecase.payments.GetPaymentMethodsUseCase
import com.learnupp.domain.usecase.payments.GetPaymentMethodsUseCaseImpl
import com.learnupp.domain.usecase.payments.ReloadPaymentMethodsUseCase
import com.learnupp.domain.usecase.payments.ReloadPaymentMethodsUseCaseImpl
import com.learnupp.domain.usecase.profile.GetProfileUseCase
import com.learnupp.domain.usecase.profile.GetProfileUseCaseImpl
import com.learnupp.domain.usecase.profile.PreloadProfileUseCase
import com.learnupp.domain.usecase.profile.PreloadProfileUseCaseImpl
import com.learnupp.domain.usecase.profile.ReloadProfileUseCase
import com.learnupp.domain.usecase.profile.ReloadProfileUseCaseImpl
import com.learnupp.domain.usecase.profile.UpdateProfileAboutUseCase
import com.learnupp.domain.usecase.profile.UpdateProfileAboutUseCaseImpl
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
import com.learnupp.domain.usecase.videos.GetGlobalVideosUseCase
import com.learnupp.domain.usecase.videos.GetGlobalVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.GetLikedVideosUseCase
import com.learnupp.domain.usecase.videos.GetLikedVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.GetMyProfileVideosUseCase
import com.learnupp.domain.usecase.videos.GetMyProfileVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.LoadMoreGlobalVideosUseCase
import com.learnupp.domain.usecase.videos.LoadMoreGlobalVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.LoadMoreMyProfileVideosUseCase
import com.learnupp.domain.usecase.videos.LoadMoreMyProfileVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.PreloadGlobalVideosUseCase
import com.learnupp.domain.usecase.videos.PreloadGlobalVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.PreloadMyProfileVideosUseCase
import com.learnupp.domain.usecase.videos.PreloadMyProfileVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.RefreshMyProfileVideosUseCase
import com.learnupp.domain.usecase.videos.RefreshMyProfileVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.ReloadVideosUseCase
import com.learnupp.domain.usecase.videos.ReloadVideosUseCaseImpl
import com.learnupp.domain.usecase.videos.ShareVideoUseCase
import com.learnupp.domain.usecase.videos.ShareVideoUseCaseImpl
import com.learnupp.domain.usecase.videos.ToggleVideoLikeUseCase
import com.learnupp.domain.usecase.videos.ToggleVideoLikeUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    // Auth
    single<RequestOtpUseCase> { RequestOtpUseCaseImpl(get()) }
    single<VerifyOtpUseCase> { VerifyOtpUseCaseImpl(get()) }
    single<CompleteProfileUseCase> { CompleteProfileUseCaseImpl(get()) }
    single<CheckUsernameUseCase> { CheckUsernameUseCaseImpl(get()) }
    single<LoginWithProviderUseCase> { LoginWithProviderUseCaseImpl(get()) }
    single<LogoutUseCase> { LogoutUseCaseImpl(get()) }

    // Profile
    single<GetProfileUseCase> { GetProfileUseCaseImpl(get()) }
    single<PreloadProfileUseCase> { PreloadProfileUseCaseImpl(get()) }
    single<ReloadProfileUseCase> { ReloadProfileUseCaseImpl(get()) }
    single<UpdateProfileAboutUseCase> { UpdateProfileAboutUseCaseImpl(get()) }
    single<UpdateProfileAboutUseCase> { UpdateProfileAboutUseCaseImpl(get()) }

    // Courses
    single<GetCoursesUseCase> { GetCoursesUseCaseImpl(get()) }
    single<PreloadCoursesUseCase> { PreloadCoursesUseCaseImpl(get()) }
    single<ReloadCoursesUseCase> { ReloadCoursesUseCaseImpl(get()) }
    single<LoadMoreCoursesUseCase> { LoadMoreCoursesUseCaseImpl(get()) }

    // Notifications
    single<GetNotificationSettingsUseCase> { GetNotificationSettingsUseCaseImpl(get()) }
    single<ReloadNotificationSettingsUseCase> { ReloadNotificationSettingsUseCaseImpl(get()) }
    single<ToggleNotificationSettingUseCase> { ToggleNotificationSettingUseCaseImpl(get()) }

    // Earnings
    single<GetEarningsSummaryUseCase> { GetEarningsSummaryUseCaseImpl(get()) }
    single<ReloadEarningsUseCase> { ReloadEarningsUseCaseImpl(get()) }

    // Payment methods
    single<GetPaymentMethodsUseCase> { GetPaymentMethodsUseCaseImpl(get()) }
    single<ReloadPaymentMethodsUseCase> { ReloadPaymentMethodsUseCaseImpl(get()) }

    // Languages
    single<GetLanguagesUseCase> { GetLanguagesUseCaseImpl(get()) }
    single<SelectLanguageUseCase> { SelectLanguageUseCaseImpl(get()) }
    single<ReloadLanguagesUseCase> { ReloadLanguagesUseCaseImpl(get()) }

    // Messages
    single<GetMessagesUseCase> { GetMessagesUseCaseImpl(get()) }
    single<PreloadMessagesUseCase> { PreloadMessagesUseCaseImpl(get()) }
    single<ReloadMessagesUseCase> { ReloadMessagesUseCaseImpl(get()) }

    // Reels
    single<GetReelsUseCase> { GetReelsUseCaseImpl(get()) }
    single<GetLikedReelsUseCase> { GetLikedReelsUseCaseImpl(get()) }
    single<PreloadReelsUseCase> { PreloadReelsUseCaseImpl(get()) }
    single<ReloadReelsUseCase> { ReloadReelsUseCaseImpl(get()) }
    single<ToggleReelLikeUseCase> { ToggleReelLikeUseCaseImpl(get()) }
    single<LoadMoreReelsUseCase> { LoadMoreReelsUseCaseImpl(get()) }
    single<ShareReelUseCase> { ShareReelUseCaseImpl(get()) }

    // Videos
    single<GetGlobalVideosUseCase> { GetGlobalVideosUseCaseImpl(get()) }
    single<GetMyProfileVideosUseCase> { GetMyProfileVideosUseCaseImpl(get()) }
    single<LoadMoreGlobalVideosUseCase> { LoadMoreGlobalVideosUseCaseImpl(get()) }
    single<LoadMoreMyProfileVideosUseCase> { LoadMoreMyProfileVideosUseCaseImpl(get()) }
    single<RefreshMyProfileVideosUseCase> { RefreshMyProfileVideosUseCaseImpl(get()) }
    single<PreloadGlobalVideosUseCase> { PreloadGlobalVideosUseCaseImpl(get()) }
    single<PreloadMyProfileVideosUseCase> { PreloadMyProfileVideosUseCaseImpl(get()) }

    single<GetLikedVideosUseCase> { GetLikedVideosUseCaseImpl(get()) }
    single<ReloadVideosUseCase> { ReloadVideosUseCaseImpl(get()) }
    single<ToggleVideoLikeUseCase> { ToggleVideoLikeUseCaseImpl(get()) }
    single<ShareVideoUseCase> { ShareVideoUseCaseImpl(get()) }
}


