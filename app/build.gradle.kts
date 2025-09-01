plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlinx.kover)
}

android {
    namespace = "com.example.mobilecodingstyleguideline"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.apiservices"
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
        jvmToolchain(17)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    testImplementation(libs.junit.jupiter)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // TS Component
    implementation(libs.ts.fixed.component)

    // Module
    implementation(project(":apiservices"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Testing
    testImplementation(libs.truth)
    testImplementation(libs.mockk)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
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