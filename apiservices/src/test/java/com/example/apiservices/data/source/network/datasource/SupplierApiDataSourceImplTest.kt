package com.example.apiservices.data.source.network.datasource

import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.data.source.network.model.response.supplier.CreateSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.DeleteSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierByIdResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierOptionResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.PutEditSupplierResponse
import com.example.apiservices.data.source.network.services.SupplierApi
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class SupplierApiDataSourceImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var supplierApiDataSource: SupplierApiDataSource

    @MockK
    private lateinit var supplierApi: SupplierApi

    private var token =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTYyNzc4MzQsImlkIjoiNjdjNTc3NjIwZmMwOWJmZWZhM2EzNzI1IiwibmFtZSI6ImFzYWQiLCJyb2xlIjoidXNlciJ9.F_5RBbRsiF2ArQockgufM1gfcwwmttI7I_-4aB2t0x8"

    @Before
    fun setUp() {
        supplierApiDataSource = SupplierApiDataSourceImpl(
            supplierApi = supplierApi
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // getSuppliers() Test
    @Test
    fun `when getSuppliers() success returns GetSupplierResponse`() = runTest {
        // Arrange
        val queryParams = GetSupplierQueryParams()
        val response = Response.success(GetSupplierResponse())

        coEvery {
            supplierApi.getSuppliers(
                token = token,
                query = queryParams.toQueryMap()
            )
        } returns response

        // Act
        val result = supplierApiDataSource.getSuppliers(token, queryParams)

        // Assert
        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `when getSuppliers() error returns GetSupplierResponse`() {
        runTest {
            // Arrange
            val queryParams = GetSupplierQueryParams()
            val response = Response.error<GetSupplierResponse>(400, "".toResponseBody())

            coEvery {
                supplierApi.getSuppliers(
                    token = token,
                    query = queryParams.toQueryMap()
                )
            } returns response

            // Act
            val result = supplierApiDataSource.getSuppliers(token, queryParams)

            // Assert
            assertThat(result).isEqualTo(response)
        }
    }

    // getSupplierById() Test
    @Test
    fun `when getSupplierById() success returns GetSupplierByIdResponse`() = runTest {
        // Arrange
        val id = "id"
        val response = Response.success(GetSupplierByIdResponse())

        coEvery {
            supplierApi.getSupplierById(
                token = token,
                id = id
            )
        } returns response

        // Act
        val result = supplierApiDataSource.getSupplierById(token, id)

        // Assert
        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `when getSupplierById() error returns GetSupplierByIdResponse`() {
        runTest {
            // Arrange
            val id = "id"
            val response = Response.error<GetSupplierByIdResponse>(400, "".toResponseBody())

            coEvery {
                supplierApi.getSupplierById(
                    token = token,
                    id = id
                )
            } returns response

            // Act
            val result = supplierApiDataSource.getSupplierById(token, id)

            // Assert
            assertThat(result).isEqualTo(response)
        }
    }

    // getSupplierOption() Test
    @Test
    fun `when getSupplierOption() success returns GetSupplierOptionResponse`() = runTest {
        // Arrange
        val queryParams = GetSupplierOptionQueryParams()
        val response = Response.success(GetSupplierOptionResponse())

        coEvery {
            supplierApi.getSupplierOption(
                token = token,
                query = queryParams.toQueryMap()
            )
        } returns response

        // Act
        val result = supplierApiDataSource.getSupplierOption(token, queryParams)

        // Assert
        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `when getSupplierOption() error returns GetSupplierOptionResponse`() {
        runTest {
            // Arrange
            val queryParams = GetSupplierOptionQueryParams()
            val response = Response.error<GetSupplierOptionResponse>(400, "".toResponseBody())

            coEvery {
                supplierApi.getSupplierOption(
                    token = token,
                    query = queryParams.toQueryMap()
                )
            } returns response

            // Act
            val result = supplierApiDataSource.getSupplierOption(token, queryParams)

            // Assert
            assertThat(result).isEqualTo(response)
        }
    }

    // createSupplier() Test
    @Test
    fun `when createSupplier() success returns CreateSupplierResponse`() = runTest {
        // Arrange
        val createBody = CreateUpdateSupplierBody()
        val response = Response.success(CreateSupplierResponse())

        coEvery {
            supplierApi.createSupplier(
                token = token,
                body = createBody
            )
        } returns response

        // Act
        val result = supplierApiDataSource.createSupplier(token, createBody)

        // Assert
        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `when createSupplier() error returns CreateSupplierResponse`() {
        runTest {
            // Arrange
            val createBody = CreateUpdateSupplierBody()
            val response = Response.error<CreateSupplierResponse>(400, "".toResponseBody())

            coEvery {
                supplierApi.createSupplier(
                    token = token,
                    body = createBody
                )
            } returns response

            // Act
            val result = supplierApiDataSource.createSupplier(token, createBody)

            // Assert
            assertThat(result).isEqualTo(response)
        }
    }

    // deleteSupplier() Test
    @Test
    fun `when deleteSupplier() success returns DeleteSupplierResponse`() = runTest {
        // Arrange
        val deleteBody = DeleteSupplierBody()
        val response = Response.success(DeleteSupplierResponse())

        coEvery {
            supplierApi.deleteSupplier(
                token = token,
                body = deleteBody
            )
        } returns response

        // Act
        val result = supplierApiDataSource.deleteSupplier(token, deleteBody)

        // Assert
        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `when deleteSupplier() error returns DeleteSupplierResponse`() {
        runTest {
            // Arrange
            val deleteBody = DeleteSupplierBody()
            val response = Response.error<DeleteSupplierResponse>(400, "".toResponseBody())

            coEvery {
                supplierApi.deleteSupplier(
                    token = token,
                    body = deleteBody
                )
            } returns response

            // Act
            val result = supplierApiDataSource.deleteSupplier(token, deleteBody)

            // Assert
            assertThat(result).isEqualTo(response)
        }
    }

    // editSupplier() Test
    @Test
    fun `when editSupplier() success returns PutEditSupplierResponse`() = runTest {
        // Arrange
        val id = "id"
        val deleteBody = CreateUpdateSupplierBody()
        val response = Response.success(PutEditSupplierResponse())

        coEvery {
            supplierApi.editSupplier(
                token = token,
                body = deleteBody,
                id = id
            )
        } returns response

        // Act
        val result = supplierApiDataSource.editSupplier(token = token, body = deleteBody, id = id)

        // Assert
        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `when editSupplier() error returns PutEditSupplierResponse`() {
        runTest {
            // Arrange
            val id = "id"
            val deleteBody = CreateUpdateSupplierBody()
            val response = Response.error<PutEditSupplierResponse>(400, "".toResponseBody())

            coEvery {
                supplierApi.editSupplier(
                    token = token,
                    body = deleteBody,
                    id = id
                )
            } returns response

            // Act
            val result =
                supplierApiDataSource.editSupplier(token = token, body = deleteBody, id = id)

            // Assert
            assertThat(result).isEqualTo(response)
        }
    }
}