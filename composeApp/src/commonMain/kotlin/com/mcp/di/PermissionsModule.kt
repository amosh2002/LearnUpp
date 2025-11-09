package com.mcp.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect fun platformModule(): Module

val permissionsModule: Module = module {
    includes(platformModule())
}