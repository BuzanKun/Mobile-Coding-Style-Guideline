package com.example.apiservices.data.repository.supplier

import com.example.apiservices.MainDispatcherRule
import com.example.apiservices.base.Result
import com.example.apiservices.data.mapper.SupplierMapper
import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.model.SupplierOptionEntity
import com.example.apiservices.data.source.network.datasource.SupplierApiDataSource
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
import com.example.apiservices.util.Constant
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class SupplierRepositoryImplTest {

    @MockK
    private lateinit var supplierRepository: SupplierRepository

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var supplierApiDataSource: SupplierApiDataSource

    @MockK
    private lateinit var supplierMapper: SupplierMapper

    private var token =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTYyNzc4MzQsImlkIjoiNjdjNTc3NjIwZmMwOWJmZWZhM2EzNzI1IiwibmFtZSI6ImFzYWQiLCJyb2xlIjoidXNlciJ9.F_5RBbRsiF2ArQockgufM1gfcwwmttI7I_-4aB2t0x8"

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcherRule.testDispatcher)
        MockKAnnotations.init(this)

        supplierRepository = SupplierRepositoryImpl(
            supplierApiDataSource = supplierApiDataSource,
            supplierMapper = supplierMapper,
            ioDispatcher = dispatcherRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // getSupplierList Test
    @Test
    fun `when getSupplierList response is successful but the body is null`() {
        runTest {
            val query = GetSupplierQueryParams()
            val apiResponse = Response.success(
                GetSupplierResponse(
                    data = GetSupplierResponse.Data(
                        data = emptyList()
                    )
                )
            )

            val apiDataList = apiResponse.body()?.data?.data.orEmpty()
            val mappedData = listOf<SupplierEntity>()
            coEvery {
                supplierApiDataSource.getSuppliers(
                    token = token,
                    query = query
                )
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 200
                coEvery { body() } returns null
            }
            every { supplierMapper.mapSupplier(apiDataList) } returns mappedData

            // Act
            val result = supplierRepository.getSupplierList(
                query
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEmpty()
        }
    }

    @Test
    fun `when getSupplierList response body has null nested data should succeed with empty list`() {
        runTest {
            // Arrange
            val query = GetSupplierQueryParams()

            coEvery {
                supplierApiDataSource.getSuppliers(token = token, query = query)
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 200
                coEvery { body() } returns GetSupplierResponse(data = null)
            }

            every { supplierMapper.mapSupplier(emptyList()) } returns emptyList()

            // Act
            val result = supplierRepository.getSupplierList(query).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEmpty()
        }
    }

    @Test
    fun `when getSupplierList response is succesful and code is 200 should emit result success`() {
        runTest {
            // Arrange
            val query = GetSupplierQueryParams()
            val apiResponse = Response.success(
                GetSupplierResponse(
                    data = GetSupplierResponse.Data(
                        data = emptyList()
                    )
                )
            )
            val apiDataList = apiResponse.body()?.data?.data.orEmpty()
            val mappedData = listOf<SupplierEntity>()
            coEvery {
                supplierApiDataSource.getSuppliers(
                    token = token,
                    query = query
                )
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 200
                coEvery { body() } returns GetSupplierResponse(
                    data = GetSupplierResponse.Data(
                        data = listOf()
                    )
                )
            }
            every { supplierMapper.mapSupplier(apiDataList) } returns mappedData

            // Act
            val result = supplierRepository.getSupplierList(
                query
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEqualTo(apiDataList)
        }
    }

    @Test
    fun `when getSupplierList response is successful but code not 200 should emit result error`() {
        runTest {
            coEvery {
                supplierApiDataSource.getSuppliers(
                    token = token,
                    query = GetSupplierQueryParams()
                )
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 400 // Not 200
                coEvery { body() } returns null
            }

            val expectedMsg = Constant.RESPONSE_ERROR

            // Act
            val result = supplierRepository.getSupplierList(
                query = GetSupplierQueryParams()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierList response is not successful should emit result error`() {
        runTest {
            coEvery {
                supplierApiDataSource.getSuppliers(
                    token = token,
                    query = GetSupplierQueryParams()
                )
            } returns mockk {
                coEvery { isSuccessful } returns false
                coEvery { code() } returns 400
                coEvery { body() } returns null
            }

            val expectedMsg = Constant.RESPONSE_ERROR

            // Act
            val result = supplierRepository.getSupplierList(
                query = GetSupplierQueryParams()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierList token is empty should emit result error`() {
        runTest {
            val emptyTokenSupplierRepository = SupplierRepositoryImpl(
                supplierApiDataSource = supplierApiDataSource,
                supplierMapper = supplierMapper,
                ioDispatcher = dispatcherRule.testDispatcher,
                token = ""
            )

            val expectedMsg = Constant.EMPTY_TOKEN_ERROR

            // Act
            val result = emptyTokenSupplierRepository.getSupplierList(
                query = GetSupplierQueryParams()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierList data source throw error should emit error`() {
        runTest {
            coEvery {
                supplierApiDataSource.getSuppliers(
                    token = token,
                    query = GetSupplierQueryParams()
                )
            } throws Exception(Constant.RESPONSE_ERROR)

            val expectedMsg = Constant.RESPONSE_ERROR

            // Act
            val result = supplierRepository.getSupplierList(
                query = GetSupplierQueryParams()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    // getSupplierById Test
    @Test
    fun `when getSupplierById response is successful but the body is null should emit result error`() {
        runTest {
            // Arrange
            coEvery {
                supplierApiDataSource.getSupplierById(
                    token = token,
                    id = "id"
                )
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 200
                coEvery { body() } returns null
            }

            val expectedMsg = Constant.RESPONSE_ERROR

            // Act
            val result = supplierRepository.getSupplierById(
                "id"
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierById response is successful and code is 200 should emit result success`() {
        runTest {
            // Arrange
            val apiResponse = mockk<GetSupplierByIdResponse> {
                coEvery { data } returns mockk()
            }

            val mappedData = SupplierEntity()

            coEvery {
                supplierApiDataSource.getSupplierById(
                    token = token,
                    id = "id"
                )
            } returns Response.success(apiResponse)

            every { supplierMapper.mapSupplierById(apiResponse.data) } returns mappedData

            // Act
            val result = supplierRepository.getSupplierById(
                "id"
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEqualTo(mappedData)
        }
    }

    @Test
    fun `when getSupplierById response is successful but code not 200 should emit result error`() {
        runTest {
            // Arrange
            coEvery {
                supplierApiDataSource.getSupplierById(token = token, id = "id")
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 400
                coEvery { body() } returns GetSupplierByIdResponse(data = GetSupplierByIdResponse.Data())
            }

            val expectedMsg = Constant.RESPONSE_ERROR

            // Act
            val result = supplierRepository.getSupplierById("id").first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierById response is not successful should emit result error`() {
        runTest {
            coEvery {
                supplierApiDataSource.getSupplierById(
                    token = token,
                    id = "id"
                )
            } returns mockk {
                coEvery { isSuccessful } returns false
                coEvery { code() } returns 400
                coEvery { body() } returns null
            }

            val expectedMsg = Constant.RESPONSE_ERROR

            // Act
            val result = supplierRepository.getSupplierById(
                path = "id"
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierById token is empty should emit result error`() {
        runTest {
            val emptyTokenSupplierRepository = SupplierRepositoryImpl(
                supplierApiDataSource = supplierApiDataSource,
                supplierMapper = supplierMapper,
                ioDispatcher = dispatcherRule.testDispatcher,
                token = ""
            )

            val expectedMsg = Constant.EMPTY_TOKEN_ERROR

            // Act
            val result = emptyTokenSupplierRepository.getSupplierById(
                path = "id"
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierById data source throw error should emit error`() {
        runTest {
            coEvery {
                supplierApiDataSource.getSupplierById(
                    token = token,
                    id = "id"
                )
            } throws Exception(Constant.UNEXPECTED_ERROR)

            val expectedMsg = Constant.UNEXPECTED_ERROR

            // Act
            val result = supplierRepository.getSupplierById(
                path = "id"
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    // getSupplierOption Test
    @Test
    fun `when getSupplierListOption response is successful but the body is null should emit result error`() {
        runTest {
            // Arrange
            coEvery {
                supplierApiDataSource.getSupplierOption(
                    token = token,
                    query = GetSupplierOptionQueryParams()
                )
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 200
                coEvery { body() } returns null
            }

            val expectedMsg = Constant.RESPONSE_ERROR

            // Act
            val result = supplierRepository.getSupplierOption(
                query = GetSupplierOptionQueryParams()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierOption() response is successful and code is 200 should emit result success`() {
        runTest {
            // Arrange
            val apiResponse = mockk<GetSupplierOptionResponse> {
                coEvery { data } returns mockk()
            }

            val mappedData = SupplierOptionEntity()

            coEvery {
                supplierApiDataSource.getSupplierOption(
                    token = token,
                    query = GetSupplierOptionQueryParams()
                )
            } returns Response.success(apiResponse)

            every { supplierMapper.mapSupplierOption(apiResponse.data) } returns mappedData

            // Act
            val result = supplierRepository.getSupplierOption(
                query = GetSupplierOptionQueryParams()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEqualTo(mappedData)
        }
    }

    @Test
    fun `when getSupplierOption() response is successful but code not 200 should emit result error`() {
        runTest {
            // Arrange
            coEvery {
                supplierApiDataSource.getSupplierOption(
                    token = token,
                    query = GetSupplierOptionQueryParams()
                )
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 400
                coEvery { body() } returns GetSupplierOptionResponse(data = GetSupplierOptionResponse.Data())
            }

            val expectedMsg = Constant.RESPONSE_ERROR

            // Act
            val result =
                supplierRepository.getSupplierOption(query = GetSupplierOptionQueryParams()).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierOption() response is not successful should emit result error`() {
        runTest {
            coEvery {
                supplierApiDataSource.getSupplierOption(
                    token = token,
                    query = GetSupplierOptionQueryParams()
                )
            } returns mockk {
                coEvery { isSuccessful } returns false
                coEvery { code() } returns 400
                coEvery { body() } returns null
            }

            val expectedMsg = Constant.RESPONSE_ERROR

            // Act
            val result = supplierRepository.getSupplierOption(
                query = GetSupplierOptionQueryParams()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierOption() token is empty should emit result error`() {
        runTest {
            val emptyTokenSupplierRepository = SupplierRepositoryImpl(
                supplierApiDataSource = supplierApiDataSource,
                supplierMapper = supplierMapper,
                ioDispatcher = dispatcherRule.testDispatcher,
                token = ""
            )

            val expectedMsg = Constant.EMPTY_TOKEN_ERROR

            // Act
            val result = emptyTokenSupplierRepository.getSupplierOption(
                query = GetSupplierOptionQueryParams()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when getSupplierOption data source throw error should emit error`() {
        runTest {
            coEvery {
                supplierApiDataSource.getSupplierOption(
                    token = token,
                    query = GetSupplierOptionQueryParams()
                )
            } throws Exception(Constant.UNEXPECTED_ERROR)

            val expectedMsg = Constant.UNEXPECTED_ERROR

            // Act
            val result = supplierRepository.getSupplierOption(
                query = GetSupplierOptionQueryParams()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    // createSupplier() Test
    @Test
    fun `when createSupplier() response is successful should emit result success`() {
        runTest {
            // Arrange
            val apiResponse = mockk<CreateSupplierResponse> {
                coEvery { data } returns mockk()
            }
            val body = CreateUpdateSupplierBody()

            coEvery {
                supplierApiDataSource.createSupplier(
                    token = token,
                    body = body
                )
            } returns Response.success(201, apiResponse)

            // Act
            val result = supplierRepository.createSupplier(body).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
        }
    }

    @Test
    fun `when createSupplier() response is successful but code is not 201 should emit result error`() {
        runTest {
            // Arrange
            val createBody = CreateUpdateSupplierBody()
            val expectedMsg = Constant.RESPONSE_ERROR

            coEvery {
                supplierApiDataSource.createSupplier(
                    token = token,
                    body = createBody
                )
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 400
                coEvery { body() } returns CreateSupplierResponse()
            }

            // Act
            val result = supplierRepository.createSupplier(createBody).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when createSupplier() response is not successful should emit result error`() {
        runTest {
            // Arrange
            val createBody = CreateUpdateSupplierBody()
            val expectedMsg = Constant.RESPONSE_ERROR

            coEvery {
                supplierApiDataSource.createSupplier(
                    token = token,
                    body = createBody
                )
            } returns Response.error(400, "".toResponseBody())

            // Act
            val result = supplierRepository.createSupplier(createBody).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when createSupplier() token is empty should emit result error`() {
        runTest {
            // Arrange
            val expectedMsg = Constant.EMPTY_TOKEN_ERROR
            val emptyTokenSupplierRepository = SupplierRepositoryImpl(
                supplierApiDataSource = supplierApiDataSource,
                supplierMapper = supplierMapper,
                ioDispatcher = dispatcherRule.testDispatcher,
                token = ""
            )

            // Act
            val result = emptyTokenSupplierRepository.createSupplier(
                body = CreateUpdateSupplierBody()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when createSupplier() data source throw error should emit result error`() {
        runTest {
            val createBody = CreateUpdateSupplierBody()
            coEvery {
                supplierApiDataSource.createSupplier(
                    token = token,
                    body = createBody
                )
            } throws Exception(Constant.UNEXPECTED_ERROR)

            val expectedMsg = Constant.UNEXPECTED_ERROR

            // Act
            val result = supplierRepository.createSupplier(
                body = createBody
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    // deleteSupplier() Test
    @Test
    fun `when deleteSupplier() response is successful should emit result success`() {
        runTest {
            // Arrange
            val apiResponse = mockk<DeleteSupplierResponse> {
                coEvery { data } returns mockk()
            }
            val body = DeleteSupplierBody()

            coEvery {
                supplierApiDataSource.deleteSupplier(
                    token = token,
                    body = body
                )
            } returns Response.success(200, apiResponse)

            // Act
            val result = supplierRepository.deleteSupplier(body).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
        }
    }

    @Test
    fun `when deleteSupplier() response is successful but code is not 200 should emit result error`() {
        runTest {
            // Arrange
            val createBody = DeleteSupplierBody()
            val expectedMsg = Constant.RESPONSE_ERROR

            coEvery {
                supplierApiDataSource.deleteSupplier(
                    token = token,
                    body = createBody
                )
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 400 // Not 200
                coEvery { body() } returns DeleteSupplierResponse()
            }

            // Act
            val result = supplierRepository.deleteSupplier(createBody).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when deleteSupplier() response is not successful should emit result error`() {
        runTest {
            // Arrange
            val createBody = DeleteSupplierBody()
            val expectedMsg = Constant.RESPONSE_ERROR

            coEvery {
                supplierApiDataSource.deleteSupplier(
                    token = token,
                    body = createBody
                )
            } returns Response.error(400, "".toResponseBody())

            // Act
            val result = supplierRepository.deleteSupplier(createBody).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when deleteSupplier() token is empty should emit result error`() {
        runTest {
            // Arrange
            val expectedMsg = Constant.EMPTY_TOKEN_ERROR
            val emptyTokenSupplierRepository = SupplierRepositoryImpl(
                supplierApiDataSource = supplierApiDataSource,
                supplierMapper = supplierMapper,
                ioDispatcher = dispatcherRule.testDispatcher,
                token = ""
            )

            // Act
            val result = emptyTokenSupplierRepository.deleteSupplier(
                body = DeleteSupplierBody()
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when deleteSupplier() data source throw error should emit result error`() {
        runTest {
            val createBody = DeleteSupplierBody()
            coEvery {
                supplierApiDataSource.deleteSupplier(
                    token = token,
                    body = createBody
                )
            } throws Exception(Constant.UNEXPECTED_ERROR)

            val expectedMsg = Constant.UNEXPECTED_ERROR

            // Act
            val result = supplierRepository.deleteSupplier(
                body = createBody
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    // editSupplier() Test
    @Test
    fun `when editSupplier() response is successful should emit result success`() {
        runTest {
            // Arrange
            val apiResponse = mockk<PutEditSupplierResponse> {
                coEvery { data } returns mockk()
            }
            val editBody = CreateUpdateSupplierBody()
            val id = "id"

            coEvery {
                supplierApiDataSource.editSupplier(
                    token = token,
                    body = editBody,
                    id = id
                )
            } returns Response.success(200, apiResponse)

            // Act
            val result = supplierRepository.editSupplier(body = editBody, path = id).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
        }
    }

    @Test
    fun `when editSupplier() response is successful but code is not 200 should emit result error`() {
        runTest {
            // Arrange
            val editBody = CreateUpdateSupplierBody()
            val id = "id"
            val expectedMsg = Constant.RESPONSE_ERROR

            coEvery {
                supplierApiDataSource.editSupplier(
                    token = token,
                    body = editBody,
                    id = id
                )
            } returns mockk {
                coEvery { isSuccessful } returns true
                coEvery { code() } returns 400 // Not 200
                coEvery { body() } returns PutEditSupplierResponse()
            }

            // Act
            val result = supplierRepository.editSupplier(body = editBody, path = id).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when editSupplier() response is not successful should emit result error`() {
        runTest {
            // Arrange
            val editBody = CreateUpdateSupplierBody()
            val id = "id"
            val expectedMsg = Constant.RESPONSE_ERROR

            coEvery {
                supplierApiDataSource.editSupplier(
                    token = token,
                    body = editBody,
                    id = id
                )
            } returns Response.error(400, "".toResponseBody())

            // Act
            val result = supplierRepository.editSupplier(body = editBody, path = id).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when editSupplier() token is empty should emit result error`() {
        runTest {
            // Arrange
            val expectedMsg = Constant.EMPTY_TOKEN_ERROR
            val emptyTokenSupplierRepository = SupplierRepositoryImpl(
                supplierApiDataSource = supplierApiDataSource,
                supplierMapper = supplierMapper,
                ioDispatcher = dispatcherRule.testDispatcher,
                token = ""
            )

            // Act
            val result = emptyTokenSupplierRepository.editSupplier(
                body = CreateUpdateSupplierBody(),
                path = "id"
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }

    @Test
    fun `when editSupplier() data source throw error should emit result error`() {
        runTest {
            val editBody = CreateUpdateSupplierBody()
            val id = "id"
            coEvery {
                supplierApiDataSource.editSupplier(
                    token = token,
                    body = editBody,
                    id = id
                )
            } throws Exception(Constant.UNEXPECTED_ERROR)

            val expectedMsg = Constant.UNEXPECTED_ERROR

            // Act
            val result = supplierRepository.editSupplier(
                body = editBody,
                path = id
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Error::class.java)
            assertThat((result as Result.Error).message).isEqualTo(expectedMsg)
        }
    }
}