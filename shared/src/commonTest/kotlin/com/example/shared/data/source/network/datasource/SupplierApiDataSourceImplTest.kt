package com.example.shared.data.source.network.datasource

import com.example.shared.base.ApiResponse
import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.shared.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.shared_test.data.source.network.services.FakeSupplierApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SupplierApiDataSourceImplTest {

    private lateinit var fakeApiService: FakeSupplierApiService
    private lateinit var dataSource: SupplierApiDataSourceImpl
    private val token = "dummy-bearer-token"

    @BeforeTest
    fun setUp() {
        fakeApiService = FakeSupplierApiService()
        dataSource = SupplierApiDataSourceImpl(fakeApiService)
    }

    // --- getSuppliers Tests ---

    @Test
    fun `getSuppliers SHOULD return ApiResponse Success WHEN Ktor service returns a successful response`() =
        runTest {
            // Arrange
            val dummySuccessBody = """
             {
              "status": 200,
              "message": "Suppliers retrieved successfully.",
              "data": {
                "totalRecords": 2,
                "data": [
                  {
                    "_id": "1",
                    "status": true,
                    "companyName": "Company A",
                    "item": [
                      {
                        "_id": "Item A",
                        "supplier_id": "1",
                        "itemName": "Item A",
                        "sku": [
                          "Sku A"
                        ]
                      }
                    ],
                    "country": "Country A",
                    "state": "State A",
                    "city": "City A",
                    "picName": "PIC A",
                    "modifiedBy": "Admin A",  
                    "created_at": "2024-01-10T10:00:00Z",
                    "updated_at": "2025-08-29T06:41:48Z"
                  },
                  {
                    "_id": "2",
                    "status": false,
                    "companyName": "Company B",
                    "item": [
                      {
                        "_id": "Item B",
                        "supplier_id": "2",
                        "itemName": "Item B",
                        "sku": [
                          "Sku B"
                        ]
                      }
                    ],
                    "country": "Country B",
                    "state": "State B",
                    "city": "City B",
                    "picName": "PIC B",
                    "modifiedBy": "Admin B",
                    "created_at": "2023-11-20T11:30:00Z",
                    "updated_at": "2025-07-15T08:20:10Z"
                  }
                ]
              }
            }
        """.trimIndent()
            val successfulResponse = mockHttpResponse(dummySuccessBody, HttpStatusCode.OK)
            fakeApiService.setHttpResponseToReturn(successfulResponse)

            // Act
            val result = dataSource.getSuppliers(token, GetSupplierQueryParams())

            // Assert
            assertIs<ApiResponse.Success<*>>(result, "Result should be ApiResponse.Success")
            val body = (result as ApiResponse.Success).body
            assertEquals("1", body.data?.data[0]?.id)
        }

    @Test
    fun `getSuppliers SHOULD return ApiResponse Error WHEN Ktor service throws an exception`() =
        runTest {
            // Arrange
            fakeApiService.setShouldThrowException(true)

            // Act
            val result = dataSource.getSuppliers(token, GetSupplierQueryParams())

            // Assert
            assertIs<ApiResponse.Error>(result, "Result should be ApiResponse.Error")
            assertEquals(
                "An unexpected error occurred.", result.exception.message,
                "The correct error message from ApiUtil should be returned"
            )
        }

    // --- getSupplierById Tests ---

    @Test
    fun `getSupplierById SHOULD return ApiResponse Success WHEN Ktor service returns a successful response`() =
        runTest {
            // Arrange
            val dummySuccessBody = """
                {
                  "status": 200,
                  "message": "Supplier detail retrieved successfully.",
                  "data": {
                    "_id": "1",
                    "status": true,
                    "companyName": "Company A",
                    "item": [
                      {
                        "_id": "Item A",
                        "supplier_id": "1",
                        "itemName": "Item A",
                        "sku": [
                          "Sku A"
                        ]
                      }
                    ],
                    "country": "Country A",
                    "state": "State A",
                    "city": "City A",
                    "zipCode": "123456",
                    "companyLocation": "Somewhere, I don't know",
                    "companyPhoneNumber": "+0123456789",
                    "picName": "PIC A",
                    "picPhoneNumber": "+0123456789",
                    "picEmail": "emailA@email.com",
                    "modifiedBy": "Admin A",
                    "created_at": "2024-01-10T10:00:00Z",
                    "updated_at": "2025-08-29T06:41:48Z"
                  }
                }
            """.trimIndent()
            val successfulResponse = mockHttpResponse(dummySuccessBody, HttpStatusCode.OK)
            fakeApiService.setHttpResponseToReturn(successfulResponse)

            // Act
            val result = dataSource.getSupplierById(token, "id")

            // Assert
            assertIs<ApiResponse.Success<*>>(result, "Result should be ApiResponse.Success")
            val body = (result as ApiResponse.Success).body
            assertEquals("Company A", body.data.companyName)
        }

    @Test
    fun `getSupplierById SHOULD return ApiResponse Error WHEN Ktor service throws an exception`() =
        runTest {
            // Arrange
            fakeApiService.setShouldThrowException(true)

            // Act
            val result = dataSource.getSupplierById(token, "id")

            // Assert
            assertIs<ApiResponse.Error>(result, "Result should be ApiResponse.Error")
            assertEquals(
                "An unexpected error occurred.", result.exception.message,
                "The correct error message from ApiUtil should be returned"
            )
        }

    // --- getSupplierOption Tests ---

    @Test
    fun `getSupplierOption SHOULD return ApiResponse Success WHEN Ktor service returns a successful response`() =
        runTest {
            // Arrange
            val dummySuccessBody = """
                {
                      "status": 200,
                      "message": "Filter options retrieved successfully.",
                      "data": {
                        "supplierOption": [
                          {
                            "label": "Apple Inc.",
                            "value": "Apple Inc."
                          },
                          {
                            "label": "Samsung Electronics",
                            "value": "Samsung Electronics"
                          },
                          {
                            "label": "GHI Indonesia",
                            "value": "GHI Indonesia"
                          }
                        ],
                        "cityOption": [
                          {
                            "label": "Cupertino",
                            "value": "Cupertino"
                          },
                          {
                            "label": "Suwon",
                            "value": "Suwon"
                          },
                          {
                            "label": "Jakarta Selatan",
                            "value": "Jakarta Selatan"
                          }
                        ],
                        "itemNameOption": [
                          {
                            "label": "iPhone 15 Pro",
                            "value": "iPhone 15 Pro"
                          },
                          {
                            "label": "MacBook Pro 16",
                            "value": "MacBook Pro 16"
                          },
                          {
                            "label": "Galaxy S25 Ultra",
                            "value": "Galaxy S25 Ultra"
                          }
                        ],
                        "modifiedByOption": [
                          {
                            "label": "Admin",
                            "value": "Admin"
                          },
                          {
                            "label": "System",
                            "value": "System"
                          }
                        ]
                      }
                    }
            """.trimIndent()
            val successfulResponse = mockHttpResponse(dummySuccessBody, HttpStatusCode.OK)
            fakeApiService.setHttpResponseToReturn(successfulResponse)

            // Act
            val result = dataSource.getSupplierOption(token, GetSupplierOptionQueryParams())

            // Assert
            assertIs<ApiResponse.Success<*>>(result, "Result should be ApiResponse.Success")
            val body = (result as ApiResponse.Success).body
            assertEquals(3, body.data.supplierOption.size)
            assertEquals(3, body.data.cityOption.size)
            assertEquals(3, body.data.itemNameOption.size)
            assertEquals(2, body.data.modifiedByOption.size)
        }

    @Test
    fun `getSupplierOption SHOULD return ApiResponse Error WHEN Ktor service throws an exception`() =
        runTest {
            // Arrange
            fakeApiService.setShouldThrowException(true)

            // Act
            val result = dataSource.getSupplierOption(token, GetSupplierOptionQueryParams())

            // Assert
            assertIs<ApiResponse.Error>(result, "Result should be ApiResponse.Error")
            assertEquals(
                "An unexpected error occurred.", result.exception.message,
                "The correct error message from ApiUtil should be returned"
            )
        }

    // --- createSupplier Tests ---

    val createRequestBody = CreateUpdateSupplierBody(
        companyName = "New Supplier",
        item = listOf(
            CreateUpdateSupplierBody.Item(
                itemName = "New Item",
                sku = listOf("New Sku")
            )
        )
    )

    @Test
    fun `createSupplier SHOULD return ApiResponse Success WHEN Ktor service succeeds`() = runTest {
        // Arrange
        val dummySuccessBody = """{"status":201,"message":"Created","data":null}"""
        val successfulResponse = mockHttpResponse(dummySuccessBody, HttpStatusCode.Created)
        fakeApiService.setHttpResponseToReturn(successfulResponse)

        // Act
        val result = dataSource.createSupplier(token, createRequestBody)

        // Assert
        assertIs<ApiResponse.Success<*>>(result, "Result should be ApiResponse.Success")
    }

    @Test
    fun `createSupplier SHOULD return ApiResponse Error WHEN Ktor service throws an exception`() =
        runTest {
            // Arrange
            fakeApiService.setShouldThrowException(true)

            // Act
            val result = dataSource.createSupplier(token, createRequestBody)

            // Assert
            assertIs<ApiResponse.Error>(result, "Result should be ApiResponse.Error")
            assertEquals(
                "An unexpected error occurred.", result.exception.message,
                "The correct error message from ApiUtil should be returned"
            )
        }

    // --- deleteSupplier Tests ---

    val deleteRequestBody = DeleteSupplierBody(listOf("1"))

    @Test
    fun `deleteSupplier SHOULD return ApiResponse Success WHEN Ktor service succeeds`() = runTest {
        // Arrange
        val dummySuccessBody = """{"status":201,"message":"Created","data":null}"""
        val successfulResponse = mockHttpResponse(dummySuccessBody, HttpStatusCode.OK)
        fakeApiService.setHttpResponseToReturn(successfulResponse)

        // Act
        val result = dataSource.deleteSupplier(token, deleteRequestBody)

        // Assert
        assertIs<ApiResponse.Success<*>>(result, "Result should be ApiResponse.Success")
    }

    @Test
    fun `deleteSupplier SHOULD return ApiResponse Error WHEN Ktor service throws an exception`() =
        runTest {
            // Arrange
            fakeApiService.setShouldThrowException(true)

            // Act
            val result = dataSource.deleteSupplier(token, deleteRequestBody)

            // Assert
            assertIs<ApiResponse.Error>(result, "Result should be ApiResponse.Error")
            assertEquals(
                "An unexpected error occurred.", result.exception.message,
                "The correct error message from ApiUtil should be returned"
            )
        }

    // --- editSupplier Tests ---

    val editSupplierBody = CreateUpdateSupplierBody(
        companyName = "Updated Supplier",
        item = listOf(
            CreateUpdateSupplierBody.Item(
                itemName = "Updated Item",
                sku = listOf("Updated Sku")
            )
        )
    )

    @Test
    fun `editSupplier SHOULD return ApiResponse Success WHEN Ktor service succeeds`() = runTest {
        // Arrange
        val dummySuccessBody = """{"status":201,"message":"Created","data":null}"""
        val successfulResponse = mockHttpResponse(dummySuccessBody, HttpStatusCode.OK)
        fakeApiService.setHttpResponseToReturn(successfulResponse)

        // Act
        val result = dataSource.editSupplier(token, "id", editSupplierBody)

        // Assert
        assertIs<ApiResponse.Success<*>>(result, "Result should be ApiResponse.Success")
    }

    @Test
    fun `editSupplier SHOULD return ApiResponse Error WHEN Ktor service throws an exception`() =
        runTest {
            // Arrange
            fakeApiService.setShouldThrowException(true)

            // Act
            val result = dataSource.editSupplier(token, "id", editSupplierBody)

            // Assert
            assertIs<ApiResponse.Error>(result, "Result should be ApiResponse.Error")
            assertEquals(
                "An unexpected error occurred.", result.exception.message,
                "The correct error message from ApiUtil should be returned"
            )
        }

    // --- editStatusSupplier ---

    val editStatusSupplierBody = PatchEditStatusSupplierBody(
        supplierID = listOf("1"),
        status = true
    )

    @Test
    fun `editStatusSupplier SHOULD return ApiResponse Success WHEN Ktor service succeeds`() =
        runTest {
            // Arrange
            val dummySuccessBody = """{"status":201,"message":"Created","data":null}"""
            val successfulResponse = mockHttpResponse(dummySuccessBody, HttpStatusCode.OK)
            fakeApiService.setHttpResponseToReturn(successfulResponse)

            // Act
            val result = dataSource.editStatusSupplier(token, editStatusSupplierBody)

            // Assert
            assertIs<ApiResponse.Success<*>>(result, "Result should be ApiResponse.Success")
        }

    @Test
    fun `editStatusSupplier SHOULD return ApiResponse Error WHEN Ktor service throws an exception`() =
        runTest {
            // Arrange
            fakeApiService.setShouldThrowException(true)

            // Act
            val result = dataSource.editStatusSupplier(token, editStatusSupplierBody)

            // Assert
            assertIs<ApiResponse.Error>(result, "Result should be ApiResponse.Error")
            assertEquals(
                "An unexpected error occurred.", result.exception.message,
                "The correct error message from ApiUtil should be returned"
            )
        }

    /**
     * A helper to create a realistic, but fake, HttpResponse for Ktor.
     * This is necessary because we can't just instantiate the HttpResponse class.
     */
    private suspend fun mockHttpResponse(
        bodyContent: String,
        statusCode: HttpStatusCode
    ): HttpResponse {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(bodyContent.toByteArray()),
                status = statusCode,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        return client.get("http://localhost/dummy")
    }
}