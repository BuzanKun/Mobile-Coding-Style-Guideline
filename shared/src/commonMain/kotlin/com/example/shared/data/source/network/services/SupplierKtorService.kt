package com.example.shared.data.source.network.services

import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class SupplierKtorService(
    private val httpClient: HttpClient
) : SupplierApiService {
    private val supplierPath = "supplier"

    override suspend fun getSuppliers(token: String, query: Map<String, String?>): HttpResponse {
        return httpClient.get(supplierPath) {
            header("Authorization", token)
            contentType(ContentType.Application.Json)
            query.forEach { (key, value) ->
                value?.let { parameter(key, it) }
            }
        }
    }

    override suspend fun getSupplierById(token: String, id: String): HttpResponse {
        return httpClient.get("$supplierPath/$id") {
            header("Authorization", token)
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun getSupplierOption(
        token: String,
        query: Map<String, Boolean?>
    ): HttpResponse {
        return httpClient.get("$supplierPath/option") {
            header("Authorization", token)
            contentType(ContentType.Application.Json)
            query.forEach { (key, value) ->
                value?.let { parameter(key, it.toString()) }
            }
        }
    }

    override suspend fun createSupplier(
        token: String,
        body: CreateUpdateSupplierBody
    ): HttpResponse {
        return httpClient.post(supplierPath) {
            header("Authorization", token)
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    override suspend fun deleteSupplier(token: String, body: DeleteSupplierBody): HttpResponse {
        return httpClient.request(supplierPath) {
            method = HttpMethod.Delete
            header("Authorization", token)
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    override suspend fun editSupplier(
        token: String,
        id: String,
        body: CreateUpdateSupplierBody
    ): HttpResponse {
        return httpClient.put("$supplierPath/$id") {
            header("Authorization", token)
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    override suspend fun editStatusSupplier(
        token: String,
        body: PatchEditStatusSupplierBody
    ): HttpResponse {
        return httpClient.patch(supplierPath) { // Adjust path if needed
            header("Authorization", token)
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}
