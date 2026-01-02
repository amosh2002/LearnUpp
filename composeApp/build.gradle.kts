import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    id("com.codingfeline.buildkonfig") version "0.17.1"
    kotlin("native.cocoapods")
}

kotlin {
    cocoapods {
        version = "1.0"
        summary = "LearnUpp Shared Module"
        homepage = ""
        ios.deploymentTarget = "15.6"

        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "ComposeApp"
            // Re-export shared models so they remain visible on the iOS side if needed.
            export(project(":sharedModel"))
        }
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            // Re-export shared models for iOS frameworks as well.
            export(project(":sharedModel"))
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.ui)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.animation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // DI
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            // Navigation
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.koin)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.tab.navigator)

            // Images for thumbnails in Reels
            implementation(libs.kamel)

            implementation(libs.androidx.datastore.preferences.core)

            // Shared domain/data models used by both client and server
            api(project(":sharedModel"))

            // Supabase Kotlin Multiplatform SDK (Auth + PostgREST)
            implementation(platform("io.github.jan-tennert.supabase:bom:2.4.0"))
            // Core Supabase client + Auth (GoTrue) + PostgREST
            implementation("io.github.jan-tennert.supabase:supabase-kt")
            implementation("io.github.jan-tennert.supabase:gotrue-kt")
            implementation("io.github.jan-tennert.supabase:postgrest-kt")
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.learnupp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.learnupp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            // Expose Android build type flag to platform-specific BuildConfig
            buildConfigField("boolean", "IS_DEBUG_MODE", "true")
        }
        getByName("release") {
            // Keep current minify behavior, just expose a flag
            isMinifyEnabled = false
            buildConfigField("boolean", "IS_DEBUG_MODE", "false")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

buildkonfig {
    packageName = "com.learnupp"
    defaultConfigs {
        // Expose build type (DEBUG/RELEASE) to common code for all platforms
        val kotlinBuildType = (project.findProperty("kotlin.build.type") as String?)?.uppercase()
        val isNativeDebug = kotlinBuildType == "DEBUG"
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN,
            "IS_DEBUG_BUILD",
            isNativeDebug.toString()
        )
        // Simple environment string we can use for things like email mode later
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "ENVIRONMENT",
            if (isNativeDebug) "\"dev\"" else "\"prod\""
        )

        // Supabase configuration (populated from local.properties)
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "SUPABASE_URL",
            "\"${getSupabaseUrl()}\""
        )
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "SUPABASE_ANON_KEY",
            "\"${getSupabaseAnonKey()}\""
        )
    }
}

fun getSupabaseUrl(): String {
    val properties = Properties()
    val file = project.rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { properties.load(it) }
    }
    return properties.getProperty("SUPABASE_URL", "")
}

fun getSupabaseAnonKey(): String {
    val properties = Properties()
    val file = project.rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { properties.load(it) }
    }
    return properties.getProperty("SUPABASE_ANON_KEY", "")
}

dependencies {
    debugImplementation(compose.uiTooling)
}

