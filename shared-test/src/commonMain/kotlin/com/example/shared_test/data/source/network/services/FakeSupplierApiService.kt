package com.example.shared_test.data.source.network.services

import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.shared.data.source.network.services.SupplierApiService
import io.ktor.client.statement.HttpResponse

/**
 * A Fake implementation of SupplierKtorService for testing the SupplierApiDataSourceImpl.
 *
 * NOTE: Since SupplierKtorService is a concrete class, this Fake inherits from it
 * and overrides its methods. The HttpClient is replaced with a dummy one that does nothing.
 */
class FakeSupplierApiService : SupplierApiService {

    // --- Test Control State ---
    private var shouldThrowException = false
    private lateinit var httpResponseToReturn: HttpResponse

    // --- Test Control Methods ---

    /**
     * Configures the fake to throw an exception when any of its methods are called.
     */
    fun setShouldThrowException(shouldThrow: Boolean) {
        shouldThrowException = shouldThrow
    }

    /**
     * Sets the specific [HttpResponse] that the fake should return.
     */
    fun setHttpResponseToReturn(response: HttpResponse) {
        httpResponseToReturn = response
    }

    // --- Interface Implementation ---

    private fun getResponseOrThrow(): HttpResponse {
        if (shouldThrowException) {
            // This simulates a network failure, timeout, etc. from Ktor.
            throw RuntimeException("Fake Ktor Service Exception")
        }
        return httpResponseToReturn
    }

    override suspend fun getSuppliers(token: String, query: Map<String, String?>): HttpResponse =
        getResponseOrThrow()

    override suspend fun getSupplierById(token: String, id: String): HttpResponse =
        getResponseOrThrow()

    override suspend fun getSupplierOption(
        token: String,
        query: Map<String, Boolean?>
    ): HttpResponse = getResponseOrThrow()

    override suspend fun createSupplier(
        token: String,
        body: CreateUpdateSupplierBody
    ): HttpResponse = getResponseOrThrow()

    override suspend fun deleteSupplier(token: String, body: DeleteSupplierBody): HttpResponse =
        getResponseOrThrow()

    override suspend fun editSupplier(
        token: String,
        id: String,
        body: CreateUpdateSupplierBody
    ): HttpResponse = getResponseOrThrow()

    override suspend fun editStatusSupplier(
        token: String,
        body: PatchEditStatusSupplierBody
    ): HttpResponse = getResponseOrThrow()
}