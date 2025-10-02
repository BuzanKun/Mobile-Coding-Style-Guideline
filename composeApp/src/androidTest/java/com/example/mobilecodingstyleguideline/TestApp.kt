@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.mobilecodingstyleguideline

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Custom Application used solely for Instrumentation Tests.
 */
class TestApp : Application()