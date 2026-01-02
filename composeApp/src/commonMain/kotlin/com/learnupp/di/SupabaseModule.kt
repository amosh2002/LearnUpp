package com.learnupp.di

import com.learnupp.BuildKonfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.dsl.module

val supabaseModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildKonfig.SUPABASE_URL,
            supabaseKey = BuildKonfig.SUPABASE_ANON_KEY
        ) {
            install(Auth) {
                // Tells the app to intercept "com.learnupp://login"
                flowType = FlowType.PKCE
                scheme = "com.learnupp"
                host = "login"
            }
            install(Postgrest)
        }
    }
}


