package com.example.shared.di

import com.example.shared.data.mapper.SupplierMapper
import org.koin.dsl.module

val mapperModule = module {
    single { SupplierMapper() }
}