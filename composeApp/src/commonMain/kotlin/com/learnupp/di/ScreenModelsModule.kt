package com.learnupp.di

import com.learnupp.ui.home.HomeScreenModel
import com.learnupp.ui.login.LoginScreenModel
import com.learnupp.ui.more.MoreScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val screenModelsModule = module {
    // Login
    single {
        LoginScreenModel(
            get()
        )
    }

    // Home
    single {
        HomeScreenModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }


    // More
    single {
        MoreScreenModel(
            get()
        )
    }
}


