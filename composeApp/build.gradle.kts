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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // TS Component
    implementation(libs.ts.fixed.component)

    // Module
    implementation(project(":shared"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kover {
    reports {
        val excludePackages = listOf(
            "dagger.hilt.internal.aggregatedroot.codegen.*",
            "hilt_aggregated_deps.*",
            "com.example.mobilecodingstyleguideline.*.di.*",
            "com.example.mobilecodingstyleguideline.*.Hilt_*",
            "com.example.mobilecodingstyleguideline.*.*_Factory*",
            "com.example.mobilecodingstyleguideline.*.*_HiltModules*",
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
            "Hilt_*",
            "*_Hilt*",
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
                    "dagger.hilt.internal.aggregatedroot.codegen.*",
                    "hilt_aggregated_deps.*",
                    "com.example.mobilecodingstyleguideline.*.di.*",
                    "com.example.mobilecodingstyleguideline.*.Hilt_*",
                    "com.example.mobilecodingstyleguideline.*.*_Factory*",
                    "com.example.mobilecodingstyleguideline.*.*_HiltModules*",
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