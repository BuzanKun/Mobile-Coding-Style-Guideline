package com.example.shared.di

import com.example.shared.domain.supplier.CreateSupplierUseCase
import com.example.shared.domain.supplier.DeleteSupplierUseCase
import com.example.shared.domain.supplier.EditStatusSupplierUseCase
import com.example.shared.domain.supplier.EditSupplierUseCase
import com.example.shared.domain.supplier.GetSupplierByIdUseCase
import com.example.shared.domain.supplier.GetSupplierOptionUseCase
import com.example.shared.domain.supplier.GetSuppliersUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetSuppliersUseCase)
    singleOf(::GetSupplierByIdUseCase)
    singleOf(::GetSupplierOptionUseCase)
    singleOf(::CreateSupplierUseCase)
    singleOf(::DeleteSupplierUseCase)
    singleOf(::EditSupplierUseCase)
    singleOf(::EditStatusSupplierUseCase)
}