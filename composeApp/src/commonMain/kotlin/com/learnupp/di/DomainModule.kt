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
import com.learnupp.domain.usecase.profile.UpdateProfileUseCase
import com.learnupp.domain.usecase.profile.UpdateProfileUseCaseImpl
import com.learnupp.domain.usecase.messages.GetMessagesUseCase
import com.learnupp.domain.usecase.messages.GetMessagesUseCaseImpl
import com.learnupp.domain.usecase.messages.PreloadMessagesUseCase
import com.learnupp.domain.usecase.messages.PreloadMessagesUseCaseImpl
import com.learnupp.domain.usecase.messages.ReloadMessagesUseCase
import com.learnupp.domain.usecase.messages.ReloadMessagesUseCaseImpl
import com.learnupp.domain.usecase.courses.GetGlobalCoursesUseCase
import com.learnupp.domain.usecase.courses.GetGlobalCoursesUseCaseImpl
import com.learnupp.domain.usecase.courses.GetMyProfileCoursesUseCase
import com.learnupp.domain.usecase.courses.GetMyProfileCoursesUseCaseImpl
import com.learnupp.domain.usecase.courses.LoadMoreGlobalCoursesUseCase
import com.learnupp.domain.usecase.courses.LoadMoreGlobalCoursesUseCaseImpl
import com.learnupp.domain.usecase.courses.LoadMoreMyProfileCoursesUseCase
import com.learnupp.domain.usecase.courses.LoadMoreMyProfileCoursesUseCaseImpl
import com.learnupp.domain.usecase.courses.PreloadGlobalCoursesUseCase
import com.learnupp.domain.usecase.courses.PreloadGlobalCoursesUseCaseImpl
import com.learnupp.domain.usecase.courses.PreloadMyProfileCoursesUseCase
import com.learnupp.domain.usecase.courses.PreloadMyProfileCoursesUseCaseImpl
import com.learnupp.domain.usecase.courses.RefreshMyProfileCoursesUseCase
import com.learnupp.domain.usecase.courses.RefreshMyProfileCoursesUseCaseImpl
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
import com.learnupp.domain.usecase.reels.GetGlobalReelsUseCase
import com.learnupp.domain.usecase.reels.GetGlobalReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.GetLikedReelsUseCase
import com.learnupp.domain.usecase.reels.GetLikedReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.GetMyProfileReelsUseCase
import com.learnupp.domain.usecase.reels.GetMyProfileReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.LoadMoreGlobalReelsUseCase
import com.learnupp.domain.usecase.reels.LoadMoreGlobalReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.LoadMoreMyProfileReelsUseCase
import com.learnupp.domain.usecase.reels.LoadMoreMyProfileReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.PreloadGlobalReelsUseCase
import com.learnupp.domain.usecase.reels.PreloadGlobalReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.PreloadMyProfileReelsUseCase
import com.learnupp.domain.usecase.reels.PreloadMyProfileReelsUseCaseImpl
import com.learnupp.domain.usecase.reels.RefreshMyProfileReelsUseCase
import com.learnupp.domain.usecase.reels.RefreshMyProfileReelsUseCaseImpl
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
    single<UpdateProfileUseCase> { UpdateProfileUseCaseImpl(get()) }
    single<PreloadProfileUseCase> { PreloadProfileUseCaseImpl(get()) }
    single<ReloadProfileUseCase> { ReloadProfileUseCaseImpl(get()) }

    // Courses
    single<GetGlobalCoursesUseCase> { GetGlobalCoursesUseCaseImpl(get()) }
    single<GetMyProfileCoursesUseCase> { GetMyProfileCoursesUseCaseImpl(get()) }
    single<LoadMoreGlobalCoursesUseCase> { LoadMoreGlobalCoursesUseCaseImpl(get()) }
    single<LoadMoreMyProfileCoursesUseCase> { LoadMoreMyProfileCoursesUseCaseImpl(get()) }
    single<RefreshMyProfileCoursesUseCase> { RefreshMyProfileCoursesUseCaseImpl(get()) }
    single<PreloadGlobalCoursesUseCase> { PreloadGlobalCoursesUseCaseImpl(get()) }
    single<PreloadMyProfileCoursesUseCase> { PreloadMyProfileCoursesUseCaseImpl(get()) }
    single<ReloadCoursesUseCase> { ReloadCoursesUseCaseImpl(get()) }

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
    single<GetGlobalReelsUseCase> { GetGlobalReelsUseCaseImpl(get()) }
    single<GetMyProfileReelsUseCase> { GetMyProfileReelsUseCaseImpl(get()) }
    single<LoadMoreGlobalReelsUseCase> { LoadMoreGlobalReelsUseCaseImpl(get()) }
    single<LoadMoreMyProfileReelsUseCase> { LoadMoreMyProfileReelsUseCaseImpl(get()) }
    single<RefreshMyProfileReelsUseCase> { RefreshMyProfileReelsUseCaseImpl(get()) }
    single<PreloadGlobalReelsUseCase> { PreloadGlobalReelsUseCaseImpl(get()) }
    single<PreloadMyProfileReelsUseCase> { PreloadMyProfileReelsUseCaseImpl(get()) }

    single<GetLikedReelsUseCase> { GetLikedReelsUseCaseImpl(get()) }
    single<ReloadReelsUseCase> { ReloadReelsUseCaseImpl(get()) }
    single<ToggleReelLikeUseCase> { ToggleReelLikeUseCaseImpl(get()) }
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


