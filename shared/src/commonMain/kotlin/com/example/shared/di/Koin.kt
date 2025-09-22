package com.example.shared.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            dataSourceModule,
            dispatcherModule,
            mapperModule,
            networkModule,
            repositoryModule,
            useCaseModule
        )
    }
}