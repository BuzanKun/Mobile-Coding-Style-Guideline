package com.example.shared.domain.supplier

import com.example.shared.base.Result
import com.example.shared.data.model.SupplierEntity
import com.example.shared.data.model.SupplierOptionEntity
import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.shared.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.shared_test.data.repository.supplier.FakeSupplierRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * A reusable list of dummy suppliers for consistent testing across this file.
 * Placing it at the top level makes it a clean, accessible constant.
 */
private val supplierDummyList = listOf(
    SupplierEntity(
        id = "1",
        status = false,
        companyName = "Company A",
        item = listOf(
            SupplierEntity.Item(
                itemName = "Item A",
                sku = listOf(
                    "Sku A"
                )
            )
        ),
        country = "Country A",
        state = "State A",
        city = "City A",
        zipCode = "Zipcode A",
        companyLocation = "Location A",
        countryCode = "Code A",
        companyPhoneNumber = "111",
        picName = "PIC A",
        picCountryCode = "Code PIC A",
        picPhoneNumber = "222",
        picEmail = "email_A@gmail.com",
        modifiedBy = "Modifier A",
        updatedAt = "Updated A",
        createdAt = "Created A"
    ),
    SupplierEntity(
        id = "2",
        status = true,
        companyName = "Company B",
        item = listOf(
            SupplierEntity.Item(
                itemName = "Item B",
                sku = listOf(
                    "Sku B"
                )
            )
        ),
        country = "Country B",
        state = "State B",
        city = "City B",
        zipCode = "Zipcode B",
        companyLocation = "Location B",
        countryCode = "Code B",
        companyPhoneNumber = "111",
        picName = "PIC B",
        picCountryCode = "Code PIC B",
        picPhoneNumber = "222",
        picEmail = "email_B@gmail.com",
        modifiedBy = "Modifier B",
        updatedAt = "Updated B",
        createdAt = "Created B"
    )
)

private const val FAKE_NETWORK_ERROR_MSG = "Fake Repository: Network Error"

/**
 * Unit tests for all supplier-related UseCases.
 */
class SupplierUseCasesTest {

    private lateinit var fakeRepository: FakeSupplierRepository
    private lateinit var getSuppliersUseCase: GetSuppliersUseCase
    private lateinit var getSupplierByIdUseCase: GetSupplierByIdUseCase
    private lateinit var createSupplierUseCase: CreateSupplierUseCase
    private lateinit var editSupplierUseCase: EditSupplierUseCase
    private lateinit var deleteSupplierUseCase: DeleteSupplierUseCase
    private lateinit var editStatusSupplierUseCase: EditStatusSupplierUseCase
    private lateinit var getSupplierOptionUseCase: GetSupplierOptionUseCase

    @BeforeTest
    fun setUp() {
        fakeRepository = FakeSupplierRepository()
        getSuppliersUseCase = GetSuppliersUseCase(fakeRepository)
        getSupplierByIdUseCase = GetSupplierByIdUseCase(fakeRepository)
        createSupplierUseCase = CreateSupplierUseCase(fakeRepository)
        editSupplierUseCase = EditSupplierUseCase(fakeRepository)
        deleteSupplierUseCase = DeleteSupplierUseCase(fakeRepository)
        editStatusSupplierUseCase = EditStatusSupplierUseCase(fakeRepository)
        getSupplierOptionUseCase = GetSupplierOptionUseCase(fakeRepository)
    }

    @AfterTest
    fun tearDown() {
        fakeRepository.clear()
    }

    // --- GetSuppliersUseCase Tests ---

    @Test
    fun `GetSuppliersUseCase SHOULD return Success with a list of suppliers`() =
        runTest {
            // Arrange
            fakeRepository.addInitialSuppliers(supplierDummyList)

            // Act
            val result = getSuppliersUseCase(GetSupplierQueryParams()).first()

            // Assert
            assertIs<Result.Success<List<SupplierEntity>>>(result)
            assertEquals(supplierDummyList, result.data)
        }

    @Test
    fun `GetSuppliersUseCase SHOULD return Error when repository fails`() =
        runTest {
            // Arrange
            fakeRepository.setShouldReturnError(true)

            // Act
            val result = getSuppliersUseCase(GetSupplierQueryParams()).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
        }

    // --- GetSupplierByIdUseCase Tests ---

    @Test
    fun `GetSupplierByIdUseCase SHOULD return Success with the correct supplier`() =
        runTest {
            // Arrange
            fakeRepository.addInitialSuppliers(supplierDummyList)
            val targetId = "2"

            // Act
            val result = getSupplierByIdUseCase(targetId).first()

            // Assert
            assertIs<Result.Success<SupplierEntity>>(result)
            assertEquals(targetId, result.data.id)
            assertEquals("Company B", result.data.companyName)
        }

    @Test
    fun `GetSupplierById SHOULD return Error WHEN there is no matched id`() =
        runTest {
            // Arrange
            fakeRepository.addInitialSuppliers(supplierDummyList)

            // Act
            val result = getSupplierByIdUseCase("unknown").first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals("Supplier with id 'unknown' not found.", result.message)
        }

    @Test
    fun `GetSupplierById SHOULD return Error WHEN repository fails to fetch`() =
        runTest {
            // Arrange
            fakeRepository.setShouldReturnError(true)

            // Act
            val result = getSupplierByIdUseCase("id").first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
        }

    // --- GetSupplierOptionUseCase Tests ---

    @Test
    fun `GetSupplierOptionUseCase SHOULD return Success with correctly mapped options`() =
        runTest {
            // Arrange
            fakeRepository.addInitialSuppliers(supplierDummyList)

            // Act
            val result = getSupplierOptionUseCase(GetSupplierOptionQueryParams()).first()

            // Assert
            assertIs<Result.Success<SupplierOptionEntity>>(result)
            val expectedOptions = listOf(
                SupplierOptionEntity.OptionData(label = "Company A", value = "Company A"),
                SupplierOptionEntity.OptionData(label = "Company B", value = "Company B")
            )
            assertEquals(expectedOptions, result.data.supplierOption)
        }

    @Test
    fun `GetSupplierOptionUseCase SHOULD return Error WHEN repository fails to fetch`() =
        runTest {
            // Arrange
            fakeRepository.setShouldReturnError(true)

            // Act
            val result = getSupplierOptionUseCase(GetSupplierOptionQueryParams()).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(FAKE_NETWORK_ERROR_MSG, result.message)
        }

    // --- CreateSupplierUseCase Tests ---

    @Test
    fun `CreateSupplierUseCase SHOULD return Success and add the supplier to the repository`() =
        runTest {
            // Arrange
            val requestBody = CreateUpdateSupplierBody(companyName = "New Company")
            assertEquals(
                0,
                fakeRepository.getSuppliersData().size,
                "PRE-CONDITION: Repository should be empty."
            )

            // Act
            val result = createSupplierUseCase(requestBody).first()

            // Assert
            assertIs<Result.Success<Unit>>(result)
            val suppliersInRepo = fakeRepository.getSuppliersData()
            assertEquals(
                1,
                suppliersInRepo.size,
                "POST-CONDITION: Repository should contain one item."
            )
            assertEquals("New Company", suppliersInRepo.first().companyName)
        }

    @Test
    fun `CreateSupplierUseCase SHOULD return Error WHEN repository fails to create`() =
        runTest {
            // 1. Arrange
            val requestBody = CreateUpdateSupplierBody(
                companyName = "New Company",
                item = listOf(
                    CreateUpdateSupplierBody.Item(
                        itemName = "New Item",
                        sku = listOf("New Sku")
                    )
                )
            )

            fakeRepository.setShouldReturnError(true)

            // Check Initial State
            assertEquals(
                0,
                fakeRepository.getSuppliersData().size,
                "Supplier Data should be empty initially"
            )

            // 2. Act
            val result = createSupplierUseCase(requestBody).first()

            // 3. Assert
            assertIs<Result.Error<*>>(result)
            assertEquals(
                0,
                fakeRepository.getSuppliersData().size,
                "Supplier Data should still be empty after failed to create"
            )
        }

    // --- DeleteSupplierUseCase Tests ---

    @Test
    fun `DeleteSupplierUseCase SHOULD return Success and remove the supplier from the repository`() =
        runTest {
            // Arrange
            fakeRepository.addInitialSuppliers(supplierDummyList)
            val requestBody = DeleteSupplierBody(supplierID = listOf("1"))
            assertEquals(
                2,
                fakeRepository.getSuppliersData().size,
                "PRE-CONDITION: Repository should contain two items."
            )

            // Act
            val result = deleteSupplierUseCase(requestBody).first()

            // Assert
            assertIs<Result.Success<Unit>>(result)
            val remainingSuppliers = fakeRepository.getSuppliersData()
            assertEquals(
                1,
                remainingSuppliers.size,
                "POST-CONDITION: Repository should contain one item."
            )
            assertEquals("2", remainingSuppliers.first().id)
        }

    @Test
    fun `DeleteSupplierUseCase SHOULD return Error WHEN repository fails to delete`() =
        runTest {
            // 1. Arrange
            fakeRepository.addInitialSuppliers(supplierDummyList)
            fakeRepository.setShouldReturnError(true)

            val requestBody = DeleteSupplierBody(supplierID = listOf("1"))

            // Check Initial State
            assertEquals(
                2,
                fakeRepository.getSuppliersData().size,
                "Initial Supplier list size should be 2"
            )

            // 2. Act
            val result = deleteSupplierUseCase(requestBody).first()

            // 3. Assert
            assertIs<Result.Error<*>>(result)
            val finalState = fakeRepository.getSuppliersData()
            assertEquals(
                2,
                finalState.size,
                "Supplier list size should still be 2 after failed to delete"
            )
        }

    // --- EditSupplierUseCase Tests ---

    @Test
    fun `EditSupplierUseCase SHOULD return Success and update the supplier in the repository`() =
        runTest {
            // Arrange
            val originalSupplier = supplierDummyList.first() // ID is "1"
            fakeRepository.addInitialSuppliers(listOf(originalSupplier))
            val newName = "Updated Company Name"
            val requestBody = CreateUpdateSupplierBody(companyName = newName)

            // Act
            val result = editSupplierUseCase("1", requestBody).first()

            // Assert
            assertIs<Result.Success<Unit>>(result)
            val updatedSupplier = fakeRepository.getSuppliersData().first()
            assertEquals(newName, updatedSupplier.companyName)
            assertEquals("1", updatedSupplier.id) // Ensure ID did not change
        }

    @Test

    fun `EditSupplierUseCase SHOULD return Error WHEN repository fails to edit`() =
        runTest {
            // 1. Arrange
            val initialSupplier = supplierDummyList.first()
            fakeRepository.addInitialSuppliers(listOf(initialSupplier))
            fakeRepository.setShouldReturnError(true)
            val requestBody = CreateUpdateSupplierBody(companyName = "Updated Name")

            // -> Initial State
            assertEquals(initialSupplier, fakeRepository.getSuppliersData().first())

            // Act
            val result = editSupplierUseCase("id", requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            val updatedSupplier = fakeRepository.getSuppliersData().first()
            assertEquals(
                initialSupplier,
                updatedSupplier,
                "Supplier detail should still be the same after edit failed"
            )
        }

    // --- EditStatusSupplierUseCase Tests ---

    @Test
    fun `EditStatusSupplierUseCase SHOULD return Success and update the supplier status`() =
        runTest {
            // Arrange
            fakeRepository.addInitialSuppliers(supplierDummyList)
            val supplierToUpdate = fakeRepository.getSuppliersData().first { it.id == "1" }
            assertEquals(
                false,
                supplierToUpdate.status,
                "PRE-CONDITION: Initial status should be false."
            )

            val newStatus = true
            val requestBody =
                PatchEditStatusSupplierBody(supplierID = listOf("1"), status = newStatus)

            // Act
            val result = editStatusSupplierUseCase(requestBody).first()

            // Assert
            assertIs<Result.Success<Unit>>(result)
            val updatedSupplier = fakeRepository.getSuppliersData().first { it.id == "1" }
            assertEquals(
                newStatus,
                updatedSupplier.status,
                "POST-CONDITION: Status should be updated to true."
            )
        }

    @Test
    fun `EditStatusSupplierUseCase SHOULD return Error and not change status when repository fails`() =
        runTest {
            // Arrange
            fakeRepository.addInitialSuppliers(supplierDummyList)
            fakeRepository.setShouldReturnError(true)
            val initialStatus = false
            val requestBody = PatchEditStatusSupplierBody(supplierID = listOf("1"), status = true)

            // Act
            val result = editStatusSupplierUseCase(requestBody).first()

            // Assert
            assertIs<Result.Error<*>>(result)
            val supplierAfterFailedAttempt =
                fakeRepository.getSuppliersData().first { it.id == "1" }
            assertEquals(
                initialStatus,
                supplierAfterFailedAttempt.status,
                "POST-CONDITION: Status should remain unchanged after failure."
            )
        }
}