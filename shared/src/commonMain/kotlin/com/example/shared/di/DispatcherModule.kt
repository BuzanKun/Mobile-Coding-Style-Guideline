package com.example.shared.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DispatcherQualifier {
    const val IO = "IODispatcher"
    const val Default = "DefaultDispatcher"
    const val Main = "MainDispatcher"
}

val dispatcherModule = module {
    single(named(DispatcherQualifier.IO)) { Dispatchers.Default }
    single(named(DispatcherQualifier.Default)) { Dispatchers.Default }
    single(named(DispatcherQualifier.Main)) { Dispatchers.Main }
}