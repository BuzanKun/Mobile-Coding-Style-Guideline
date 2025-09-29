package com.example.shared.data.repository.supplier

import com.example.shared.base.Result
import com.example.shared.data.mapper.SupplierMapper
import com.example.shared.data.model.SupplierEntity
import com.example.shared.data.model.SupplierOptionEntity
import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.shared.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.shared.data.source.network.model.response.supplier.GetSupplierResponse
import com.example.shared.util.Constant
import com.example.shared_test.data.source.network.datasource.FakeSupplierApiDataSource
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * A reusable DTO for populating the FakeDataSource.
 */
private val supplierDto = listOf(
    GetSupplierResponse.Data.Data(
        id = "1",
        status = false,
        companyName = "Company A",
        item = listOf(
            GetSupplierResponse.Data.Data.Item(
                itemName = "Item A",
                sku = listOf(
                    "Sku A"
                )
            )
        ),
        country = "Country A",
        state = "State A",
        city = "City A",
        picName = "PIC A",
        modifiedBy = "Modifier A",
        updatedAt = "Updated A",
        createdAt = "Created A"
    ),
    GetSupplierResponse.Data.Data(
        id = "2",
        status = true,
        companyName = "Company B",
        item = listOf(
            GetSupplierResponse.Data.Data.Item(
                itemName = "Item B",
                sku = listOf(
                    "Sku B"
                )
            )
        ),
        country = "Country B",
        state = "State B",
        city = "City B",
        picName = "PIC B",
        modifiedBy = "Modifier B",
        updatedAt = "Updated B",
        createdAt = "Created B"
    )
)

private const val FAKE_UNEXPECTED_ERROR_MESSAGE = "Fake Unexpected Exception"
private const val FAKE_NETWORK_ERROR_MSG = "Fake Network Error"
private const val RESPONSE_ERROR_MSG = Constant.RESPONSE_ERROR

/**
 * Unit tests for the `SupplierRepositoryImpl`.
 *
 * This test class uses a `FakeSupplierApiDataSource` to provide a controlled environment
 * for testing the repository's logic, including data mapping and error handling.
 * It is fully KMP-compatible.
 */
class SupplierRepositoryImplTest {

    private lateinit var fakeDataSource: FakeSupplierApiDataSource
    private val supplierMapper: SupplierMapper = SupplierMapper()
    private lateinit var supplierRepository: SupplierRepository

    @BeforeTest
    fun setUp() {
        fakeDataSource = FakeSupplierApiDataSource()

        supplierRepository = SupplierRepositoryImpl(
            supplierApiDataSource = fakeDataSource,
            supplierMapper = supplierMapper,
            ioDispatcher = Dispatchers.Unconfined
        )
    }

    @AfterTest
    fun tearDown() {
        fakeDataSource.clear()
    }

    // --- getSupplierList Tests ---

    @Test
    fun `getSupplierList SHOULD return Success with mapped entities WHEN data source succeeds`() =
        runTest {
            // Arrange
            fakeDataSource.addInitialSuppliers(supplierDto)

            // Act
            val result = supplierRepository.getSupplierList(GetSupplierQueryParams()).first()

            // Assert
            assertIs<Result.Success<List<SupplierEntity>>>(result)
            assertEquals(2, result.data.size, "Should return one mapped supplier")
            assertEquals(
                "Company A",
                result.data.first().companyName,
                "Company name should be correctly mapped"
            )
            assertEquals("1", result.data.first().id)
        }

    @Test
    fun `getSupplierList SHOULD return Success with an empty list WHEN data source returns no data`() =
        runTest {
            // Arrange: No data is added to the fake data source to simulate empty data

            // Act
            val result = supplierRepository.getSupplierList(GetSupplierQueryParams()).first()

            // Assert
            assertIs<Result.Success<List<SupplierEntity>>>(result)
            assertEquals(0, result.data.size, "Should return an empty list")
        }

    @Test
    fun `getSupplierList SHOULD return Error WHEN data source returns success with a non-200 status code`() =
        runTest {
            // Arrange
            fakeDataSource.addInitialSuppliers(supplierDto) // Add data so it's a valid success response
            fakeDataSource.setHttpStatusCodeToReturn(HttpStatusCode.BadRequest) // Simulate a server error

            // Act
            val result = supplierRepository.getSupplierList(GetSupplierQueryParams()).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(RESPONSE_ERROR_MSG, result.message)
        }

    @Test
    fun `getSupplierList SHOULD return Error WHEN data source returns Error Response`() = runTest {
        // Arrange
        fakeDataSource.setShouldReturnError(true)

        // Act
        val result = supplierRepository.getSupplierList(GetSupplierQueryParams()).first()

        // Assert
        assertIs<Result.Error<*>>(result)
        assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
    }

    @Test
    fun `getSupplierList SHOULD return Error WHEN data source throws an unexpected exception`() =
        runTest {
            // Arrange
            fakeDataSource.setShouldThrowException(true)

            // Act
            val result = supplierRepository.getSupplierList(GetSupplierQueryParams()).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_UNEXPECTED_ERROR_MESSAGE, result.message)
        }

    // --- getSupplierById Tests ---

    @Test
    fun `getSupplierById SHOULD return Success with mapped entity WHEN data source succeeds`() =
        runTest {
            // Arrange
            fakeDataSource.addInitialSuppliers(supplierDto)

            // Act
            val result = supplierRepository.getSupplierById("1").first()

            // Assert
            assertIs<Result.Success<SupplierEntity>>(result)
            assertEquals(
                "Company A",
                result.data.companyName,
                "Company name should be correctly mapped"
            )
            assertEquals("1", result.data.id)
        }

    @Test
    fun `getSupplierById SHOULD return Error with an empty data WHEN there is no matching id`() =
        runTest {
            // Arrange
            fakeDataSource.addInitialSuppliers(supplierDto)
            // Act
            val result = supplierRepository.getSupplierById("unknown").first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals("Supplier with id 'unknown' not found.", result.message)
        }

    @Test
    fun `getSupplierById SHOULD return Error WHEN data source returns success with a non-200 status code`() =
        runTest {
            // Arrange
            fakeDataSource.addInitialSuppliers(supplierDto)
            fakeDataSource.setHttpStatusCodeToReturn(HttpStatusCode.BadRequest)

            // Act
            val result = supplierRepository.getSupplierById("1").first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(RESPONSE_ERROR_MSG, result.message)
        }

    @Test
    fun `getSupplierById SHOULD return Error WHEN data source returns Error Response`() = runTest {
        // Arrange
        fakeDataSource.setShouldReturnError(true)

        // Act
        val result = supplierRepository.getSupplierById("1").first()

        // Assert
        assertIs<Result.Error<*>>(result)
        assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
    }

    @Test
    fun `getSupplierById SHOULD return Error WHEN data source throws an unexpected exception`() =
        runTest {
            // Arrange
            fakeDataSource.setShouldThrowException(true)

            // Act
            val result = supplierRepository.getSupplierById("1").first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_UNEXPECTED_ERROR_MESSAGE, result.message)
        }

    // --- getSupplierOption Tests ---

    @Test
    fun `getSupplierOption SHOULD return Success with mapped entities WHEN data source succeeds`() =
        runTest {
            // Arrange
            fakeDataSource.addInitialSuppliers(supplierDto)

            // Act
            val result =
                supplierRepository.getSupplierOption(GetSupplierOptionQueryParams()).first()

            // Assert
            assertIs<Result.Success<SupplierOptionEntity>>(result)
            val expectedOptions = listOf(
                SupplierOptionEntity.OptionData(label = "Company A", value = "Company A"),
                SupplierOptionEntity.OptionData(label = "Company B", value = "Company B")
            )
            assertEquals(expectedOptions, result.data.supplierOption)
        }

    @Test
    fun `getSupplierOption SHOULD return Success with an empty option WHEN data source returns no data`() =
        runTest {
            // Arrange: No data is added to the fake data source to simulate empty data

            // Act
            val result =
                supplierRepository.getSupplierOption(GetSupplierOptionQueryParams()).first()

            // Assert
            assertIs<Result.Success<SupplierOptionEntity>>(result)
            assertEquals(0, result.data.supplierOption.size, "No supplier option")
            assertEquals(0, result.data.modifiedByOption.size, "No modifier option")
            assertEquals(0, result.data.cityOption.size, "No city option")
            assertEquals(0, result.data.itemNameOption.size, "No item name option")
        }

    @Test
    fun `getSupplierOption SHOULD return Error WHEN data source returns success with a non-200 status code`() =
        runTest {
            // Arrange
            fakeDataSource.addInitialSuppliers(supplierDto)
            fakeDataSource.setHttpStatusCodeToReturn(HttpStatusCode.BadRequest)

            // Act
            val result =
                supplierRepository.getSupplierOption(GetSupplierOptionQueryParams()).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(RESPONSE_ERROR_MSG, result.message)
        }

    @Test
    fun `getSupplierOption SHOULD return Error WHEN data source returns Error Response`() =
        runTest {
            // Arrange
            fakeDataSource.setShouldReturnError(true)

            // Act
            val result =
                supplierRepository.getSupplierOption(GetSupplierOptionQueryParams()).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
        }

    @Test
    fun `getSupplierOption SHOULD return Error WHEN data source throws an unexpected exception`() =
        runTest {
            // Arrange
            fakeDataSource.setShouldThrowException(true)

            // Act
            val result =
                supplierRepository.getSupplierOption(GetSupplierOptionQueryParams()).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_UNEXPECTED_ERROR_MESSAGE, result.message)
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
    fun `createSupplier SHOULD return Success WHEN data source succeeds`() = runTest {
        // Arrange
        val requestBody = createRequestBody
        fakeDataSource.setHttpStatusCodeToReturn(HttpStatusCode.Created) // Code 201
        assertEquals(
            0,
            fakeDataSource.getSuppliersData().size,
            "PRE-CONDITION: Data source should be empty."
        )

        // Act
        val result = supplierRepository.createSupplier(requestBody).first()

        // Assert
        assertIs<Result.Success<Unit>>(result)
        val suppliersAfter = fakeDataSource.getSuppliersData()
        assertEquals(1, suppliersAfter.size, "POST-CONDITION: Data source should contain one item.")
        assertEquals("New Supplier", suppliersAfter.first().companyName)
    }

    @Test
    fun `createSupplier SHOULD return Error WHEN data source returns success with a non-200 status code`() =
        runTest {
            // Arrange
            val requestBody = createRequestBody
            fakeDataSource.addInitialSuppliers(supplierDto)
            fakeDataSource.setHttpStatusCodeToReturn(HttpStatusCode.BadRequest)

            // Act
            val result = supplierRepository.createSupplier(requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(RESPONSE_ERROR_MSG, result.message)
        }

    @Test
    fun `createSupplier SHOULD return Error WHEN data source returns Error Response`() = runTest {
        // Arrange
        val requestBody = createRequestBody
        fakeDataSource.setShouldReturnError(true)

        // Act
        val result = supplierRepository.createSupplier(requestBody).first()

        // Assert
        assertIs<Result.Error<*>>(result)
        assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
    }

    @Test
    fun `createSupplier SHOULD return Error WHEN data source throws an unexpected exception`() =
        runTest {
            // Arrange
            val requestBody = createRequestBody
            fakeDataSource.setShouldThrowException(true)

            // Act
            val result = supplierRepository.createSupplier(requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_UNEXPECTED_ERROR_MESSAGE, result.message)
        }

    // --- deleteSupplier Tests ---

    val deleteRequestBody = DeleteSupplierBody(listOf("1"))

    @Test
    fun `deleteSupplier SHOULD return Success WHEN data source succeeds`() = runTest {
        // Arrange
        val requestBody = deleteRequestBody
        fakeDataSource.addInitialSuppliers(supplierDto)
        assertEquals(
            2,
            fakeDataSource.getSuppliersData().size,
            "PRE-CONDITION: Data source should have two items."
        )

        // Act
        val result = supplierRepository.deleteSupplier(requestBody).first()

        // Assert
        assertIs<Result.Success<Unit>>(result)
        val suppliersAfter = fakeDataSource.getSuppliersData()
        assertEquals(
            1,
            suppliersAfter.size,
            "POST-CONDITION: Data source should have one item left."
        )
        assertEquals("2", suppliersAfter.first().id)
    }

    @Test
    fun `deleteSupplier SHOULD return Error WHEN data source returns success with a non-200 status code`() =
        runTest {
            // Arrange
            val requestBody = deleteRequestBody
            fakeDataSource.setHttpStatusCodeToReturn(HttpStatusCode.BadRequest)

            // Act
            val result = supplierRepository.deleteSupplier(requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(RESPONSE_ERROR_MSG, result.message)
        }

    @Test
    fun `deleteSupplier SHOULD return Error WHEN data source returns Error Response`() = runTest {
        // Arrange
        val requestBody = deleteRequestBody
        fakeDataSource.setShouldReturnError(true)

        // Act
        val result = supplierRepository.deleteSupplier(requestBody).first()

        // Assert
        assertIs<Result.Error<*>>(result)
        assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
    }

    @Test
    fun `deleteSupplier SHOULD return Error WHEN data source throws an unexpected exception`() =
        runTest {
            // Arrange
            val requestBody = deleteRequestBody
            fakeDataSource.setShouldThrowException(true)

            // Act
            val result = supplierRepository.deleteSupplier(requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_UNEXPECTED_ERROR_MESSAGE, result.message)
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
    fun `editSupplier SHOULD return Success WHEN data source succeeds`() = runTest {
        // Arrange
        val requestBody = editSupplierBody
        fakeDataSource.addInitialSuppliers(supplierDto)
        val originalName = fakeDataSource.getSuppliersData().first { it.id == "1" }.companyName
        assertEquals("Company A", originalName, "PRE-CONDITION: Original name should be correct.")

        // Act
        val result = supplierRepository.editSupplier("1", requestBody).first()

        // Assert
        assertIs<Result.Success<Unit>>(result)
        val supplierAfter = fakeDataSource.getSuppliersData().first { it.id == "1" }
        assertEquals(
            "Updated Supplier",
            supplierAfter.companyName,
            "POST-CONDITION: Name should be updated."
        )
    }

    @Test
    fun `editSupplier SHOULD return Error WHEN data source returns success with a non-200 status code`() =
        runTest {
            // Arrange
            val requestBody = editSupplierBody
            fakeDataSource.addInitialSuppliers(supplierDto)
            fakeDataSource.setHttpStatusCodeToReturn(HttpStatusCode.BadRequest)

            // Act
            val result = supplierRepository.editSupplier("1", requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(RESPONSE_ERROR_MSG, result.message)
        }

    @Test
    fun `editSupplier SHOULD return Error WHEN data source returns Error Response`() = runTest {
        // Arrange
        val requestBody = editSupplierBody
        fakeDataSource.setShouldReturnError(true)

        // Act
        val result = supplierRepository.editSupplier("1", requestBody).first()

        // Assert
        assertIs<Result.Error<*>>(result)
        assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
    }

    @Test
    fun `editSupplier SHOULD return Error WHEN data source throws an unexpected exception`() =
        runTest {
            // Arrange
            val requestBody = editSupplierBody
            fakeDataSource.setShouldThrowException(true)

            // Act
            val result = supplierRepository.editSupplier("1", requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_UNEXPECTED_ERROR_MESSAGE, result.message)
        }

    // --- editStatusSupplier Tests ---

    val editStatusbody = PatchEditStatusSupplierBody(
        supplierID = listOf("1"),
        status = true
    )

    @Test
    fun `editStatusSupplier SHOULD return Success WHEN data source succeeds`() = runTest {
        // Arrange
        val requestBody = editStatusbody
        fakeDataSource.addInitialSuppliers(supplierDto)
        val originalStatus = fakeDataSource.getSuppliersData().first { it.id == "1" }.status
        assertEquals(false, originalStatus, "PRE-CONDITION: Initial status should be false.")

        // Act
        val result = supplierRepository.editStatusSupplier(requestBody).first()

        // Assert
        assertIs<Result.Success<Unit>>(result)
        val supplierAfter = fakeDataSource.getSuppliersData().first { it.id == "1" }
        assertEquals(
            true,
            supplierAfter.status,
            "POST-CONDITION: Status should be updated to true."
        )
    }

    @Test
    fun `editStatusSupplier SHOULD return Error WHEN data source returns success with a non-200 status code`() =
        runTest {
            // Arrange
            val requestBody = editStatusbody
            fakeDataSource.addInitialSuppliers(supplierDto)
            fakeDataSource.setHttpStatusCodeToReturn(HttpStatusCode.BadRequest)

            // Act
            val result = supplierRepository.editStatusSupplier(requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(RESPONSE_ERROR_MSG, result.message)
        }

    @Test
    fun `editStatusSupplier SHOULD return Error WHEN data source returns Error Response`() =
        runTest {
            // Arrange
            val requestBody = editStatusbody
            fakeDataSource.setShouldReturnError(true)

            // Act
            val result = supplierRepository.editStatusSupplier(requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
        }

    @Test
    fun `editStatusSupplier SHOULD return Error WHEN data source throws an unexpected exception`() =
        runTest {
            // Arrange
            val requestBody = editStatusbody
            fakeDataSource.setShouldThrowException(true)

            // Act
            val result = supplierRepository.editStatusSupplier(requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_UNEXPECTED_ERROR_MESSAGE, result.message)
        }
}