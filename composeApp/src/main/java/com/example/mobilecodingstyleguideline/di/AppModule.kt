package com.example.mobilecodingstyleguideline.di

import com.example.mobilecodingstyleguideline.ui.screen.createsupplier.viewmodel.CreateSupplierViewModel
import com.example.mobilecodingstyleguideline.ui.screen.home.viewmodel.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        HomeViewModel(
            getSuppliersUseCase = get(),
            getSupplierOptionUseCase = get(),
            deleteSupplierUseCase = get(),
            editStatusSupplierUseCase = get()
        )
    }

    viewModel {
        CreateSupplierViewModel(
            getSuppliersUseCase = get(),
            getSupplierByIdUseCase = get(),
            createSupplierUseCase = get(),
            editSupplierUseCase = get()
        )
    }
}