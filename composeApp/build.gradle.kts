plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.kover)
}

android {
    namespace = "com.example.mobilecodingstyleguideline"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mobilecodingstyleguideline"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.mobilecodingstyleguideline.TestRunner"
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "env"
    productFlavors {
        create("development") {
            dimension = "env"
        }
        create("staging") {
            dimension = "env"
        }
        create("production") {
            dimension = "env"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlin {
        jvmToolchain(21)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    // FIX: Add packaging options to exclude duplicate resources from test dependencies
    // This is often needed when dealing with Ktor, Coroutines, and other libraries in tests.
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/LICENSE.md"
        }
    }
}

dependencies {
    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Koin
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Ktor (needed if your app module uses it directly)
    implementation(project.dependencies.platform(libs.ktor.bom))
    implementation(libs.ktor.client.core)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // TS Component
    implementation(libs.ts.fixed.component)

    // Project Modules
    implementation(project(":shared"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // --- Testing Dependencies ---
    // Unit Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

    // Instrumentation (UI) Tests
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.mockk) // MockK for android tests if needed
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.koin.test.junit4)

    // Add koin-test for instrumentation tests to use KoinTest, get(), etc.
    androidTestImplementation(libs.koin.test)

    // Add the shared-test module to access fakes and test utilities
    androidTestImplementation(project(":shared-test"))

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Kover configuration remains the same
kover {
    reports {
        val excludePackages = listOf(
            "com.example.mobilecodingstyleguideline.*.di.*",
            "com.example.mobilecodingstyleguideline.*.*_Factory*",
            "com.example.mobilecodingstyleguideline.*.*Module_*",
            "com.example.mobilecodingstyleguideline.*.*MembersInjector*",
            "com.example.mobilecodingstyleguideline.*.*_Impl*",
            "com.example.mobilecodingstyleguideline.ComposableSingletons*",
            "com.example.mobilecodingstyleguideline.BuildConfig*",
            "com.example.mobilecodingstyleguideline.*.Fake*",
            "com.example.mobilecodingstyleguideline.app.ComposableSingletons*",
            "*_*Factory.*",
            "*_*Factory*",
            "*_Factory.*",
            "*.navigation.*"
        )

        val includePackages = listOf(
            "com.example.mobilecodingstyleguideline.data.*",
            "com.example.mobilecodingstyleguideline.domain*",
            "com.example.mobilecodingstyleguideline.ui.*.viewmodel",
            "com.example.mobilecodingstyleguideline.ui.*.uistate",
            "com.example.mobilecodingstyleguideline.ui.*.model",
        )

        filters {
            excludes {
                classes(
                    "com.example.mobilecodingstyleguideline.*.di.*",
                    "com.example.mobilecodingstyleguideline.*.*_Factory*",
                    "com.example.mobilecodingstyleguideline.*.*Module_*",
                    "com.example.mobilecodingstyleguideline.*.*MembersInjector*",
                    "com.example.mobilecodingstyleguideline.*.*_Impl*",
                    "com.example.mobilecodingstyleguideline.ComposableSingletons*",
                    "com.example.mobilecodingstyleguideline.BuildConfig*",
                    "com.example.mobilecodingstyleguideline.*.Fake*",
                    "com.example.mobilecodingstyleguideline.app.ComposableSingletons*"
                )

                packages(
                    "kotlinx.coroutines.*"
                )
            }
        }

        variant("developmentDebug") {
            xml {
                onCheck = true
                xmlFile = file("result.xml")
            }
            filters {
                excludes {
                    classes(excludePackages)
                    packages("kotlinx.coroutines.*")
                }

                includes {
                    packages(
                        includePackages
                    )
                }
            }
            html {
                title = "Kover Report"
                charset = "UTF-8"
                onCheck = true
            }
        }
    }
}