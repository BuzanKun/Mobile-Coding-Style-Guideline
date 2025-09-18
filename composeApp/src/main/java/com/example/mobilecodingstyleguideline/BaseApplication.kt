package com.example.mobilecodingstyleguideline

import android.app.Application
import com.example.mobilecodingstyleguideline.di.appModule
import com.example.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BaseApplication)
            androidLogger()
            modules(appModule)
        }
    }
}