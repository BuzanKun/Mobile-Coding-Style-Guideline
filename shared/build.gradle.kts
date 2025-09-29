plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.example.shared"
        compileSdk = 36
        minSdk = 24

        withJava()

        withHostTestBuilder {}

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "sharedKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html

    sourceSets {
        // Shared Module Dependencies
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                // KMP libraries
                implementation(libs.kotlinx.coroutines.core) // Coroutines
                implementation(libs.kotlinx.serialization.json)

                // Ktor
                implementation(project.dependencies.platform(libs.ktor.bom))
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.auth)


                // Koin
                implementation(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.core)
            }
        }

        // Shared Module Test Dependencies
        commonTest {
            dependencies {
                // Shared Test Module
                implementation(project(":shared-test"))

                // Base Kotlin Test
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)

                // Ktor
                implementation(libs.ktor.client.mock)
                implementation(libs.ktor.client.auth)
            }
        }

        // Android Specific Dependencies
        androidMain {
            dependencies {
                // Ktor
                implementation(libs.ktor.client.android)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.okhttp.logging.interceptor)

                // Koin
                implementation(libs.koin.android)
                implementation(libs.koin.compose)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.junit)
            }
        }

        // iOS Specific Dependencies
        iosMain {
            dependencies {
                // Add iOS-specific dependencies here.
            }
        }
    }
}