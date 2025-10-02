package com.example.mobilecodingstyleguideline

import com.example.mobilecodingstyleguideline.di.appModule
import com.example.shared.data.source.network.datasource.SupplierApiDataSource
import com.example.shared.di.DispatcherQualifier
import com.example.shared.di.dataSourceModule
import com.example.shared.di.dispatcherModule
import com.example.shared.di.mapperModule
import com.example.shared.di.networkModule
import com.example.shared.di.repositoryModule
import com.example.shared.di.useCaseModule
import com.example.shared_test.data.source.network.datasource.FakeSupplierApiDataSource
import com.example.shared_test.util.DataDummy
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

val productionModule = module {
    includes(
        dispatcherModule,
        networkModule,
        dataSourceModule,
        repositoryModule,
        mapperModule,
        useCaseModule,
        appModule
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
val testAppModule = module {
    // Override Coroutine Dispatchers for synchronous execution in tests
    single(named(DispatcherQualifier.IO)) { UnconfinedTestDispatcher() }
    single(named(DispatcherQualifier.Main)) { UnconfinedTestDispatcher() }
    single(named(DispatcherQualifier.Default)) { UnconfinedTestDispatcher() }

    // Override the network data source with our controllable fake instance
    single {
        FakeSupplierApiDataSource().apply {
            addInitialSuppliers(DataDummy.INITIAL_SUPPLIER_DTO)
            setHttpStatusCodeToReturn(HttpStatusCode.OK)
        }
    }

    single<SupplierApiDataSource> { get<FakeSupplierApiDataSource>() }

}