package com.example.apiservices.data.source.network.services

import com.example.apiservices.util.Constant
import com.google.common.truth.Truth.assertThat
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SupplierApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: SupplierApi
    private val token = Constant.BEARER_TOKEN

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupplierApi::class.java)
    }

    @After
    fun tearDown() {
        unmockkAll()
        mockWebServer.shutdown()
    }

    // GetSupplier Test
    @Test
    fun `when getSuppliers should return success response`() = runBlocking {
        // Arrange
        val mockResponse = MockResponse()
            .setBody(
                """
                    {
                      "status": 200,
                      "message": "Suppliers retrieved successfully.",
                      "data": {
                        "totalRecords": 2,
                        "data": [
                          {
                            "_id": "651d0ffc1a4c401880927719",
                            "status": true,
                            "companyName": "Apple Inc.",
                            "item": [
                              {
                                "_id": "item_001",
                                "supplier_id": "651d0ffc1a4c401880927719",
                                "itemName": "iPhone 15 Pro",
                                "sku": [
                                  "IP15-BLK-256",
                                  "IP15-BLU-512"
                                ]
                              },
                              {
                                "_id": "item_002",
                                "supplier_id": "651d0ffc1a4c401880927719",
                                "itemName": "MacBook Pro 16",
                                "sku": [
                                  "MBP16-M3-1TB"
                                ]
                              }
                            ],
                            "country": "USA",
                            "state": "California",
                            "city": "Cupertino",
                            "picName": "Tim Cook",
                            "modifiedBy": "Admin",
                            "created_at": "2024-01-10T10:00:00Z",
                            "updated_at": "2025-08-29T06:41:48Z"
                          },
                          {
                            "_id": "651d0abc2b5d501880928820",
                            "status": false,
                            "companyName": "Samsung Electronics",
                            "item": [
                              {
                                "_id": "item_003",
                                "supplier_id": "651d0abc2b5d501880928820",
                                "itemName": "Galaxy S25 Ultra",
                                "sku": [
                                  "GS25-TGR-512"
                                ]
                              }
                            ],
                            "country": "South Korea",
                            "state": "Gyeonggi-do",
                            "city": "Suwon",
                            "picName": "Lee Jae-yong",
                            "modifiedBy": "System",
                            "created_at": "2023-11-20T11:30:00Z",
                            "updated_at": "2025-07-15T08:20:10Z"
                          }
                        ]
                      }
                    }
                """.trimIndent()
            )
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        // Act
        val response = api.getSuppliers(token)

        // Assert
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()).isNotNull()
        assertThat(response.body()?.data?.data).isNotEmpty()
        assertThat(response.body()?.data?.data?.size).isEqualTo(2)
        assertThat(response.body()?.data?.data?.first()?.companyName).isEqualTo("Apple Inc.")
    }

    // SupplierOption Test
    @Test
    fun `when getSupplierOption should return success response`() = runBlocking {
        // Arrange
        val mockResponse = MockResponse()
            .setBody(
                """
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
            )
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        // Act
        val response = api.getSupplierOption(token)

        // Assert
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()).isNotNull()
        assertThat(response.body()?.data?.supplierOption).isNotEmpty()
        assertThat(response.body()?.data?.supplierOption?.size).isEqualTo(3)
        assertThat(response.body()?.data?.supplierOption?.first()?.label).isEqualTo("Apple Inc.")
        assertThat(response.body()?.data?.supplierOption?.first()?.value).isEqualTo("Apple Inc.")

        assertThat(response.body()?.data?.itemNameOption).isNotEmpty()
        assertThat(response.body()?.data?.itemNameOption?.size).isEqualTo(3)
        assertThat(response.body()?.data?.itemNameOption?.first()?.label).isEqualTo("iPhone 15 Pro")
        assertThat(response.body()?.data?.itemNameOption?.first()?.value).isEqualTo("iPhone 15 Pro")

        assertThat(response.body()?.data?.cityOption).isNotEmpty()
        assertThat(response.body()?.data?.cityOption?.size).isEqualTo(3)
        assertThat(response.body()?.data?.cityOption?.first()?.label).isEqualTo("Cupertino")
        assertThat(response.body()?.data?.cityOption?.first()?.value).isEqualTo("Cupertino")

        assertThat(response.body()?.data?.modifiedByOption).isNotEmpty()
        assertThat(response.body()?.data?.modifiedByOption?.size).isEqualTo(2)
        assertThat(response.body()?.data?.modifiedByOption?.first()?.label).isEqualTo("Admin")
        assertThat(response.body()?.data?.modifiedByOption?.first()?.value).isEqualTo("Admin")
    }
}