package com.example.shared.di

import com.example.shared.data.source.network.datasource.SupplierApiDataSource
import com.example.shared.data.source.network.datasource.SupplierApiDataSourceImpl
import com.example.shared.data.source.network.services.SupplierKtorService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSourceModule = module {
    singleOf(::SupplierApiDataSourceImpl).bind(SupplierApiDataSource::class)
    single { SupplierKtorService(get()) }
}