package com.example.apiservices.di

import com.example.apiservices.data.source.network.datasource.SupplierApiDataSource
import com.example.apiservices.data.source.network.datasource.SupplierApiDataSourceImpl
import com.example.apiservices.data.source.network.services.SupplierApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideSupplyApiDataSource(supplierApi: SupplierApi): SupplierApiDataSource {
        return SupplierApiDataSourceImpl(supplierApi = supplierApi)
    }
}