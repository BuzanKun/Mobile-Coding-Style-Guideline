plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlinx.kover)
}

android {
    namespace = "com.example.apiservices"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)

    // Testing
    testImplementation(libs.mockwebserver)
    testImplementation(libs.okhttp)
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
}


kover {
    reports {
        val excludePackages = listOf(
            "dagger.hilt.internal.aggregatedroot.codegen.*",
            "hilt_aggregated_deps.*",
            "com.example.apiservices.*.di.*",
            "com.example.apiservices.*.Hilt_*",
            "com.example.apiservices.*.*_Factory*",
            "com.example.apiservices.*.*_HiltModules*",
            "com.example.apiservices.*.*Module_*",
            "com.example.apiservices.*.*MembersInjector*",
            "com.example.apiservices.*.*_Impl*",
            "com.example.apiservices.ComposableSingletons*",
            "com.example.apiservices.BuildConfig*",
            "com.example.apiservices.*.Fake*",
            "com.example.apiservices.app.ComposableSingletons*",
            "*_*Factory.*",
            "*_*Factory*",
            "*_Factory.*",
            "Hilt_*",
            "*_Hilt*",
            "*.navigation.*"
        )

        val includePackages = listOf(
            "com.example.apiservices.data.*",
            "com.example.apiservices.domain*",
            "com.example.apiservices.ui.*.viewmodel",
            "com.example.apiservices.ui.*.uistate",
            "com.example.apiservices.ui.*.model",
        )

        filters {
            excludes {
                classes(
                    "dagger.hilt.internal.aggregatedroot.codegen.*",
                    "hilt_aggregated_deps.*",
                    "com.example.apiservices.*.di.*",
                    "com.example.apiservices.*.Hilt_*",
                    "com.example.apiservices.*.*_Factory*",
                    "com.example.apiservices.*.*_HiltModules*",
                    "com.example.apiservices.*.*Module_*",
                    "com.example.apiservices.*.*MembersInjector*",
                    "com.example.apiservices.*.*_Impl*",
                    "com.example.apiservices.ComposableSingletons*",
                    "com.example.apiservices.BuildConfig*",
                    "com.example.apiservices.*.Fake*",
                    "com.example.apiservices.app.ComposableSingletons*"
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