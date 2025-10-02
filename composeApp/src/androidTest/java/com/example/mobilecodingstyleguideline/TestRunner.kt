package com.example.mobilecodingstyleguideline

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Custom AndroidJUnitRunner to ensure our TestApp is used for instrumentation tests.
 * This is configured in the app's build.gradle.kts file.
 */
class TestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        // Force the use of our custom TestApp for all instrumentation tests.
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}
