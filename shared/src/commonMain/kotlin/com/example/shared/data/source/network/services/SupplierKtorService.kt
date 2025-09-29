package com.example.shared.data.source.network.services

import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

class SupplierKtorService(
    private val httpClient: HttpClient
) : SupplierApiService {
    private val supplierPath = "supplier"

    override suspend fun getSuppliers(query: Map<String, String?>): HttpResponse {
        return httpClient.get(supplierPath) {
            query.forEach { (key, value) ->
                value?.let { parameter(key, it) }
            }
        }
    }

    override suspend fun getSupplierById(id: String): HttpResponse {
        return httpClient.get("$supplierPath/$id")
    }

    override suspend fun getSupplierOption(
        query: Map<String, Boolean?>
    ): HttpResponse {
        return httpClient.get("$supplierPath/option") {
            query.forEach { (key, value) ->
                value?.let { parameter(key, it.toString()) }
            }
        }
    }

    override suspend fun createSupplier(
        body: CreateUpdateSupplierBody
    ): HttpResponse {
        return httpClient.post(supplierPath) {
            setBody(body)
        }
    }

    override suspend fun deleteSupplier(body: DeleteSupplierBody): HttpResponse {
        return httpClient.request(supplierPath) {
            method = HttpMethod.Delete
            setBody(body)
        }
    }

    override suspend fun editSupplier(

        id: String,
        body: CreateUpdateSupplierBody
    ): HttpResponse {
        return httpClient.put("$supplierPath/$id") {
            setBody(body)
        }
    }

    override suspend fun editStatusSupplier(

        body: PatchEditStatusSupplierBody
    ): HttpResponse {
        return httpClient.patch(supplierPath) {
            setBody(body)
        }
    }
}
