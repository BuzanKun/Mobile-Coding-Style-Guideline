package com.example.shared.data.source.network.services

import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import io.ktor.client.statement.HttpResponse

interface SupplierApiService {
    suspend fun getSuppliers(token: String, query: Map<String, String?> = mapOf()): HttpResponse
    suspend fun getSupplierById(token: String, id: String): HttpResponse
    suspend fun getSupplierOption(
        token: String,
        query: Map<String, Boolean?> = mapOf()
    ): HttpResponse

    suspend fun createSupplier(token: String, body: CreateUpdateSupplierBody): HttpResponse
    suspend fun deleteSupplier(token: String, body: DeleteSupplierBody): HttpResponse
    suspend fun editSupplier(
        token: String,
        id: String,
        body: CreateUpdateSupplierBody
    ): HttpResponse

    suspend fun editStatusSupplier(token: String, body: PatchEditStatusSupplierBody): HttpResponse
}