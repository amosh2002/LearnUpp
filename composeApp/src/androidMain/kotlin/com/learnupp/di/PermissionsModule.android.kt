package com.learnupp.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun platformModule(): Module = module {
//    single<PermissionDelegate>(named(Permission.LOCATION_SERVICE_ON.name)) {
//        LocationServicePermissionDelegate(
//            locationManager = get(),
//        )
//    }
//
//    single<PermissionDelegate>(named(Permission.LOCATION_FOREGROUND.name)) {
//        LocationForegroundPermissionDelegate()
//    }
//
//    single<LocationService> { AndroidLocationService(androidContext()) }
//
//    single<PermissionsService> { AndroidPermissionsServiceImpl() }
}