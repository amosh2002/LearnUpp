package com.learnupp.di

import org.koin.core.module.Module

fun sharedModules(): List<Module> = listOf(
	dataModule,
	domainModule,
	screenModelsModule,
	permissionsModule,
    supabaseModule
)


