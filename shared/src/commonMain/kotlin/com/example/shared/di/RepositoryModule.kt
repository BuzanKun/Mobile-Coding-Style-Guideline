package com.example.shared.di

import com.example.shared.data.repository.supplier.SupplierRepository
import com.example.shared.data.repository.supplier.SupplierRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<SupplierRepository> {
        SupplierRepositoryImpl(
            supplierApiDataSource = get(),
            supplierMapper = get(),
            ioDispatcher = get(named(DispatcherQualifier.IO))
        )
    }
}