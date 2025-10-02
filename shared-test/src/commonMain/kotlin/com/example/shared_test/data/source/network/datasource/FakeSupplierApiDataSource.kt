package com.example.shared_test.data.source.network.datasource

import com.example.shared.base.ApiResponse
import com.example.shared.data.source.network.datasource.SupplierApiDataSource
import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.shared.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.shared.data.source.network.model.response.supplier.CreateSupplierResponse
import com.example.shared.data.source.network.model.response.supplier.DeleteSupplierResponse
import com.example.shared.data.source.network.model.response.supplier.GetSupplierByIdResponse
import com.example.shared.data.source.network.model.response.supplier.GetSupplierOptionResponse
import com.example.shared.data.source.network.model.response.supplier.GetSupplierResponse
import com.example.shared.data.source.network.model.response.supplier.PatchEditStatusSupplierResponse
import com.example.shared.data.source.network.model.response.supplier.PutEditSupplierResponse
import io.ktor.client.call.HttpClientCall
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.util.date.GMTDate
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A Fake implementation of the SupplierApiDataSource for testing.
 *
 * This class simulates the behavior of a real API data source by managing an
 * in-memory list. It allows for controlled testing of success and failure
 * scenarios without making actual network calls. It is fully KMP-compatible.
 */


private const val FAKE_UNEXPECTED_ERROR_MESSAGE = "Fake Unexpected Exception"
private const val FAKE_NETWORK_ERROR_MSG = "Fake Network Error"

@OptIn(ExperimentalTime::class, InternalAPI::class)
class FakeSupplierApiDataSource : SupplierApiDataSource {

    // --- State Management ---

    private val suppliersData = mutableListOf<GetSupplierResponse.Data.Data>()
    private var shouldReturnError = false
    private var shouldThrowException = false
    private var httpStatusCodeToReturn: HttpStatusCode = HttpStatusCode.OK
    private var nextId = 1

    // --- Test Control Methods ---

    /** Toggles the fake to return a generic ApiResponse.Error. */
    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    /** Toggles the fake to throw an unhandled RuntimeException. */
    fun setShouldThrowException(value: Boolean) { // <-- NEW
        shouldThrowException = value
    }

    /** Sets a specific HTTP status code to be returned with successful responses. */
    fun setHttpStatusCodeToReturn(statusCode: HttpStatusCode) { // <-- NEW
        httpStatusCodeToReturn = statusCode
    }

    /** Pre-populates the in-memory list with data before a test runs. */
    fun addInitialSuppliers(initialData: List<GetSupplierResponse.Data.Data>) {
        suppliersData.addAll(initialData)
        nextId = (initialData.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1
    }

    /** Resets the state of the fake. Call this in your test's @BeforeTest block. */
    fun clear() {
        suppliersData.clear()
        shouldReturnError = false
        shouldThrowException = false
        nextId = 1
    }

    fun getSuppliersData(): List<GetSupplierResponse.Data.Data> = suppliersData.toList()

    /** Helper to build a dummy HttpResponse for ApiResponse.Success */
    private fun buildSuccessfulResponse(statusCode: HttpStatusCode = HttpStatusCode.OK): HttpResponse {
        return object : HttpResponse() {
            override val call: HttpClientCall get() = TODO("Not needed for fake")
            override val coroutineContext = CoroutineScope(Dispatchers.Unconfined).coroutineContext
            override val headers: Headers = headersOf()
            override val requestTime: GMTDate = GMTDate.START
            override val responseTime: GMTDate = GMTDate.START
            override val rawContent: ByteReadChannel get() = TODO("Not yet implemented")
            override val status: HttpStatusCode = statusCode
            override val version: HttpProtocolVersion = HttpProtocolVersion.HTTP_1_1
        }
    }


    // --- Interface Implementation ---

    override suspend fun getSuppliers(
        query: GetSupplierQueryParams
    ): ApiResponse<GetSupplierResponse> {
        if (shouldThrowException) throw RuntimeException(FAKE_UNEXPECTED_ERROR_MESSAGE) // <-- NEW
        if (shouldReturnError) return ApiResponse.Error(Exception(FAKE_NETWORK_ERROR_MSG))

        val response = GetSupplierResponse(
            data = GetSupplierResponse.Data(
                totalRecords = suppliersData.size,
                data = suppliersData.toList()
            ),
            message = "Success",
            status = httpStatusCodeToReturn.value
        )
        return ApiResponse.Success(buildSuccessfulResponse(httpStatusCodeToReturn), response)
    }

    override suspend fun getSupplierById(
        id: String
    ): ApiResponse<GetSupplierByIdResponse> {
        if (shouldThrowException) throw RuntimeException(FAKE_UNEXPECTED_ERROR_MESSAGE)
        if (shouldReturnError) return ApiResponse.Error(Exception(FAKE_NETWORK_ERROR_MSG))

        val supplier = suppliersData.find { it.id == id }

        return if (supplier != null) {
            val responseData = GetSupplierByIdResponse.Data(
                id = supplier.id,
                companyName = supplier.companyName,
                status = supplier.status,
                // Map all other relevant fields from `supplier` DTO
            )
            val response = GetSupplierByIdResponse(
                data = responseData,
                message = "Success",
                status = httpStatusCodeToReturn.value
            )
            ApiResponse.Success(buildSuccessfulResponse(httpStatusCodeToReturn), response)
        } else {
            ApiResponse.Error(Exception("Supplier with id '$id' not found."))
        }
    }

    override suspend fun getSupplierOption(
        query: GetSupplierOptionQueryParams
    ): ApiResponse<GetSupplierOptionResponse> {
        if (shouldThrowException) throw RuntimeException(FAKE_UNEXPECTED_ERROR_MESSAGE)
        if (shouldReturnError) return ApiResponse.Error(Exception(FAKE_NETWORK_ERROR_MSG))

        val responseData = GetSupplierOptionResponse.Data(
            supplierOption = suppliersData.map {
                GetSupplierOptionResponse.Data.OptionData(
                    it.companyName,
                    it.companyName
                )
            },
            cityOption = suppliersData.map {
                GetSupplierOptionResponse.Data.OptionData(
                    it.city,
                    it.city
                )
            }.distinct(),
            itemNameOption = suppliersData.flatMap { supplier ->
                supplier.item.map {
                    GetSupplierOptionResponse.Data.OptionData(
                        it.itemName,
                        it.itemName
                    )
                }
            }.distinct(),
            modifiedByOption = suppliersData.map {
                GetSupplierOptionResponse.Data.OptionData(
                    it.modifiedBy,
                    it.modifiedBy
                )
            }
        )
        val response = GetSupplierOptionResponse(
            data = responseData,
            message = "Success",
            status = httpStatusCodeToReturn.value
        )
        return ApiResponse.Success(buildSuccessfulResponse(httpStatusCodeToReturn), response)
    }

    override suspend fun createSupplier(
        body: CreateUpdateSupplierBody
    ): ApiResponse<CreateSupplierResponse> {
        if (shouldThrowException) throw RuntimeException(FAKE_UNEXPECTED_ERROR_MESSAGE) // <-- NEW
        if (shouldReturnError) return ApiResponse.Error(Exception(FAKE_NETWORK_ERROR_MSG))

        val newSupplierData = GetSupplierResponse.Data.Data(
            id = (nextId++).toString(),
            companyName = body.companyName,
            status = true,
            item = body.item.map {
                GetSupplierResponse.Data.Data.Item(
                    itemName = it.itemName,
                    sku = it.sku
                )
            },
            country = body.country,
            state = body.state,
            city = body.city,
            picName = body.picName,
            modifiedBy = "Test User",
            createdAt = Clock.System.now().toString(),
            updatedAt = Clock.System.now().toString()
        )
        suppliersData.add(newSupplierData)

        val response = CreateSupplierResponse(
            data = null,
            message = "Success Create Supplier",
            status = httpStatusCodeToReturn.value
        )
        return ApiResponse.Success(buildSuccessfulResponse(httpStatusCodeToReturn), response)
    }

    override suspend fun deleteSupplier(
        body: DeleteSupplierBody
    ): ApiResponse<DeleteSupplierResponse> {
        if (shouldThrowException) throw RuntimeException(FAKE_UNEXPECTED_ERROR_MESSAGE) // <-- NEW
        if (shouldReturnError) return ApiResponse.Error(Exception(FAKE_NETWORK_ERROR_MSG))

        suppliersData.removeAll { it.id in body.supplierID }

        val response = DeleteSupplierResponse(
            data = null,
            message = "Success Delete Supplier",
            status = httpStatusCodeToReturn.value
        )
        return ApiResponse.Success(buildSuccessfulResponse(httpStatusCodeToReturn), response)
    }

    override suspend fun editSupplier(
        id: String,
        body: CreateUpdateSupplierBody
    ): ApiResponse<PutEditSupplierResponse> {
        if (shouldThrowException) throw RuntimeException(FAKE_UNEXPECTED_ERROR_MESSAGE) // <-- NEW
        if (shouldReturnError) return ApiResponse.Error(Exception(FAKE_NETWORK_ERROR_MSG))

        val index = suppliersData.indexOfFirst { it.id == id }
        return if (index != -1) {
            val existing = suppliersData[index]
            suppliersData[index] = existing.copy(
                companyName = body.companyName,
                item = body.item.map {
                    GetSupplierResponse.Data.Data.Item(
                        itemName = it.itemName,
                        sku = it.sku
                    )
                },
                // ... map other fields
                updatedAt = Clock.System.now().toString()
            )
            // The real API returns a body with null data, so we simulate that.
            val response = PutEditSupplierResponse(
                data = null,
                message = "Success Edit Supplier",
                status = httpStatusCodeToReturn.value
            )
            ApiResponse.Success(buildSuccessfulResponse(httpStatusCodeToReturn), response)
        } else {
            ApiResponse.Error(Exception("Supplier with id '$id' not found."))
        }
    }

    override suspend fun editStatusSupplier(
        body: PatchEditStatusSupplierBody
    ): ApiResponse<PatchEditStatusSupplierResponse> {
        if (shouldThrowException) throw RuntimeException(FAKE_UNEXPECTED_ERROR_MESSAGE) // <-- NEW
        if (shouldReturnError) return ApiResponse.Error(Exception(FAKE_NETWORK_ERROR_MSG))

        suppliersData.forEachIndexed { index, supplier ->
            if (supplier.id in body.supplierID) {
                suppliersData[index] = supplier.copy(status = body.status)
            }
        }

        val response = PatchEditStatusSupplierResponse(
            data = null,
            message = "Success",
            status = httpStatusCodeToReturn.value
        )
        return ApiResponse.Success(buildSuccessfulResponse(httpStatusCodeToReturn), response)
    }
}