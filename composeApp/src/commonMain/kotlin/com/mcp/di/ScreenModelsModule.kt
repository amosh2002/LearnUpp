package com.mcp.di

import com.mcp.ui.home.HomeScreenModel
import com.mcp.ui.login.LoginScreenModel
import com.mcp.ui.more.MoreScreenModel
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


