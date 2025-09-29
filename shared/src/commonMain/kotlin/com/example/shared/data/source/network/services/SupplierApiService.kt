package com.example.shared.data.source.network.services

import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import io.ktor.client.statement.HttpResponse

interface SupplierApiService {
    suspend fun getSuppliers(query: Map<String, String?> = mapOf()): HttpResponse
    suspend fun getSupplierById(id: String): HttpResponse
    suspend fun getSupplierOption(
        query: Map<String, Boolean?> = mapOf()
    ): HttpResponse

    suspend fun createSupplier(body: CreateUpdateSupplierBody): HttpResponse
    suspend fun deleteSupplier(body: DeleteSupplierBody): HttpResponse
    suspend fun editSupplier(
        id: String,
        body: CreateUpdateSupplierBody
    ): HttpResponse

    suspend fun editStatusSupplier(body: PatchEditStatusSupplierBody): HttpResponse
}