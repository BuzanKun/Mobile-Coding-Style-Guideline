package com.example.mobilecodingstyleguideline.ui.screen.createsupplier.viewmodel

import app.cash.turbine.test
import com.example.apiservices.base.Result
import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.domain.supplier.CreateSupplierUseCase
import com.example.apiservices.domain.supplier.EditSupplierUseCase
import com.example.apiservices.domain.supplier.GetSupplierByIdUseCase
import com.example.apiservices.domain.supplier.GetSuppliersUseCase
import com.example.apiservices.util.Constant
import com.example.mobilecodingstyleguideline.MainDispatcherRule
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierCallback
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormData
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormError
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormOption
import com.google.common.truth.Truth.assertThat
import com.tagsamurai.common.model.OptionData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateSupplierViewModelTest {

    private lateinit var viewModel: CreateSupplierViewModel

    private lateinit var callback: CreateSupplierCallback

    @get:Rule
    private val mainDispatcher = MainDispatcherRule()

    @MockK
    private lateinit var getSupplierUseCase: GetSuppliersUseCase

    @MockK
    private lateinit var getSupplierByIdUseCase: GetSupplierByIdUseCase

    @MockK
    private lateinit var createSupplierUseCase: CreateSupplierUseCase

    @MockK
    private lateinit var editSupplierUseCase: EditSupplierUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = CreateSupplierViewModel(
            getSuppliersUseCase = getSupplierUseCase,
            getSupplierByIdUseCase = getSupplierByIdUseCase,
            createSupplierUseCase = createSupplierUseCase,
            editSupplierUseCase = editSupplierUseCase
        )

        callback = viewModel.getCallback()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun arrangeGetSupplierUseCase(
        params: GetSupplierQueryParams,
        result: Result<List<SupplierEntity>>
    ) {
        coEvery {
            getSupplierUseCase(params)
        } returns flowOf(result)
    }

    private fun arrangeGetSupplierByIdUseCase(
        id: String,
        result: Result<SupplierEntity>
    ) {
        coEvery {
            getSupplierByIdUseCase(id)
        } returns flowOf(result)
    }

    private fun arrangeCreateSupplierUseCase(
        body: CreateUpdateSupplierBody,
        result: Result<Unit>
    ) {
        coEvery {
            createSupplierUseCase(body)
        } returns flowOf(result)
    }

    private fun arrangeEditSupplierUseCase(
        id: String,
        body: CreateUpdateSupplierBody,
        result: Result<Unit>
    ) {
        coEvery {
            editSupplierUseCase(id = id, body = body)
        } returns flowOf(result)
    }

    // Init Test
    @Test
    fun `when init is called without supplierId, assetId should be empty and isEditForm should be false`() {
        runTest {
            // Arrange
            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Success(listOf())
            )

            // Act
            viewModel.init()

            // Assert
            assertThat(viewModel.uiState.value.assetId).isEmpty()
            assertThat(viewModel.uiState.value.submitState).isNull()
            assertThat(viewModel.uiState.value.isEditForm).isFalse()
        }
    }

    @Test
    fun `when init is called with supplierId, assetId should be supplierId and isEditFform should be true`() {
        runTest {
            // Arrange
            val supplierId = "id"
            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Success(listOf())
            )

            arrangeGetSupplierByIdUseCase(
                id = supplierId,
                result = Result.Success(SupplierEntity())
            )

            // Act
            viewModel.init(supplierId)

            // Assert
            coVerify(exactly = 1) {
                getSupplierByIdUseCase(supplierId)
            }
        }
    }

    // GetSupplyById Test
    @Test
    fun `when getSupplyById returns success, isLoadingOverlay should be false and isEditForm should be True`() {
        runTest {
            // Arrange
            val id = "id"
            arrangeGetSupplierByIdUseCase(
                id = id,
                result = Result.Success(SupplierEntity())
            )

            viewModel.uiState.test {
                val initialState = awaitItem()
                assertThat(initialState.isLoadingOverlay).isFalse()

                // Act
                viewModel.getSupplyById(id)

                val loadingState = awaitItem()
                assertThat(loadingState.isLoadingOverlay).isTrue()

                // Assert
                val finalState = awaitItem()
                assertThat(finalState.isLoadingOverlay).isFalse()
                assertThat(finalState.isEditForm).isTrue()
                assertThat(finalState.assetId).isEqualTo(id)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when getSupplyById returns success, add supplier data to form data`() {
        runTest {
            // Assert
            val id = "id"

            val supplier = SupplierEntity(
                id = "id",
                status = true,
                companyName = "name",
                item = listOf(
                    SupplierEntity.Item(
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                zipCode = "zipCode",
                companyLocation = "companyLocation",
                companyPhoneNumber = "companyPhoneNumber",
                picName = "picName",
                picPhoneNumber = "picPhoneNumber",
                picEmail = "picEmail",
                updatedAt = "updatedAt",
                createdAt = "createdAt",
            )

            val expectedFormData = CreateSupplierFormData(
                companyName = "name",
                items = listOf(
                    CreateSupplierFormData.Item(
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                zipCode = "zipCode",
                companyLocation = "companyLocation",
                companyPhoneNumber = "companyPhoneNumber",
                picName = "picName",
                picPhoneNumber = "picPhoneNumber",
                picEmail = "picEmail"
            )

            arrangeGetSupplierByIdUseCase(
                id = id,
                result = Result.Success(supplier)
            )

            viewModel.uiState.test {
                val initialState = awaitItem()
                assertThat(initialState.isLoadingOverlay).isFalse()

                // Act
                viewModel.getSupplyById(id)

                val actingState = awaitItem()
                assertThat(actingState.isLoadingOverlay).isTrue()

                // Assert
                val finalState = awaitItem()
                assertThat(finalState.isLoadingOverlay).isFalse()
                assertThat(finalState.formData).isEqualTo(expectedFormData)

            }
        }
    }

    @Test
    fun `when getSupplyId returns error, isLoadingOverlay should be false`() {
        runTest {
            // Arrange
            val id = "id"

            arrangeGetSupplierByIdUseCase(
                id = id,
                result = Result.Error(Constant.UNEXPECTED_ERROR)
            )

            viewModel.uiState.test {
                val initialState = awaitItem()
                assertThat(initialState.isLoadingOverlay).isFalse()

                // Act
                viewModel.getSupplyById(id)

                val actingState = awaitItem()
                assertThat(actingState.isLoadingOverlay).isTrue()

                // Assert
                val finalState = awaitItem()
                assertThat(finalState.isLoadingOverlay).isFalse()
            }
        }
    }

    // getFormOption Test
    @Test
    fun `when getSuppliersUseCase returns success, map data to form options`() {
        runTest {
            // Arrange
            val supplier = listOf(
                SupplierEntity(
                    id = "id",
                    status = true,
                    companyName = "name",
                    item = listOf(
                        SupplierEntity.Item(
                            itemName = "itemName",
                            itemSku = listOf("sku")
                        )
                    ),
                    country = "country",
                    state = "state",
                    city = "city",
                    zipCode = "zipCode",
                    companyLocation = "companyLocation",
                    companyPhoneNumber = "companyPhoneNumber",
                    picName = "picName",
                    picPhoneNumber = "picPhoneNumber",
                    picEmail = "picEmail",
                    updatedAt = "updatedAt",
                    createdAt = "createdAt",
                )
            )

            val expectedOption = CreateSupplierFormOption(
                itemNameList = listOf(
                    OptionData(
                        label = "itemName",
                        value = "itemName"
                    )
                ),
                itemSkuList = listOf(
                    OptionData(
                        label = "sku",
                        value = "sku"
                    )
                ),
                country = listOf(
                    OptionData(
                        label = "country",
                        value = "country"
                    )
                ),
                state = listOf(
                    OptionData(
                        label = "state",
                        value = "state"
                    )
                ),
                city = listOf(
                    OptionData(
                        label = "city",
                        value = "city"
                    )
                )
            )

            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Success(supplier)
            )

            viewModel.uiState.test {
                val initialState = awaitItem()
                assertThat(initialState.isLoadingFormOption).isFalse()

                // Act
                viewModel.getFormOption()

                val actingState = awaitItem()
                assertThat(actingState.isLoadingFormOption).isTrue()

                // Assert
                val finalState = awaitItem()
                assertThat(finalState.isLoadingFormOption).isFalse()
                assertThat(finalState.formOption).isEqualTo(expectedOption)
            }
        }
    }

    @Test
    fun `when getSupplierUseCase returns success, isLoadingFormOption should be false and form options should be empty`() {
        runTest {
            // Arrange
            val emptyFormOption = CreateSupplierFormOption()

            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Error(Constant.UNEXPECTED_ERROR)
            )

            viewModel.uiState.test {
                val initialState = awaitItem()
                assertThat(initialState.isLoadingFormOption).isFalse()

                // Act
                viewModel.getFormOption()

                val actingState = awaitItem()
                assertThat(actingState.isLoadingFormOption).isTrue()

                // Assert
                val finalState = awaitItem()
                assertThat(finalState.isLoadingFormOption).isFalse()
                assertThat(finalState.formOption).isEqualTo(emptyFormOption)
            }
        }
    }

    // ClearField Test
    @Test
    fun `when clearField is called, formData and formError should be empty and submitState should be null`() {
        runTest {
            // Arrannge
            val emptyFormData = CreateSupplierFormData()
            val emptyFormError = CreateSupplierFormError()

            // Act
            callback.onClearField()

            // Assert
            assertThat(viewModel.uiState.value.formData).isEqualTo(emptyFormData)
            assertThat(viewModel.uiState.value.formError).isEqualTo(emptyFormError)
            assertThat(viewModel.uiState.value.submitState).isNull()
        }
    }

    // ResetMessageState Test
    @Test
    fun `when onResetMessageState is called, submitState should be null`() {
        runTest {
            // Act
            callback.onResetMessageState()

            // Arrange
            assertThat(viewModel.uiState.value.submitState).isNull()
        }
    }

    // UpdateFormData Test
    @Test
    fun `when onUpdateFormData is called, populate form data and clear form error`() {
        runTest {
            // Arrange
            val emptyFormError = CreateSupplierFormError()
            val formData = CreateSupplierFormData(
                companyName = "name",
                items = listOf(
                    CreateSupplierFormData.Item(
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                zipCode = "zipCode",
                companyLocation = "companyLocation",
                companyPhoneNumber = "companyPhoneNumber",
                picName = "picName",
                picPhoneNumber = "picPhoneNumber",
                picEmail = "picEmail"
            )

            // Act
            callback.onUpdateFormData(formData)

            // Assert
            assertThat(viewModel.uiState.value.formData).isEqualTo(formData)
            assertThat(viewModel.uiState.value.formError).isEqualTo(emptyFormError)
        }
    }

    // UpdateStayOnForm Test
    @Test
    fun `if updateStayOnForm is called when isStayOnForm is false, make it true`() {
        runTest {
            // Initial State (False)
            assertThat(viewModel.uiState.value.isStayOnForm).isFalse()

            // Act
            callback.onUpdateStayOnForm()

            // Assert
            assertThat(viewModel.uiState.value.isStayOnForm).isTrue()
        }
    }

    // Submit Form Test
    @Test
    fun `when submitForm is called with invalid form data, formError should be updated`() {
        runTest {
            // Arrange
            val invalidData = CreateSupplierFormData(
                companyName = "",
                zipCode = "ZipCode with more than 15 characters",
                companyLocation = "Company Address with more than 120 characters is invalid. To be valid, company Address should be less than 120 characters.",
                companyPhoneNumber = "Phone number with more than 15 characters",
                picName = "PIC name with more than 30 characters",
                picPhoneNumber = "PIC phone number with more than 15 characters",
                picEmail = "Incorrect Email Format"
            )

            val expectedFormError = CreateSupplierFormError(
                companyName = "Company name must not be empty",
                itemName = listOf(
                    "You must pick an item name"
                ),
                itemSku = listOf(
                    null
                ),
                zipCode = "Max. 15 characters",
                address = "Max. 120 characters",
                phoneNumber = "Max. 15 characters",
                picName = "Max. 30 characters",
                picPhoneNumber = "Max. 15 characters",
                picEmail = "Email format is incorrect"
            )

            // Act
            callback.onUpdateFormData(invalidData)
            callback.onSubmitForm()

            // Assert
            assertThat(viewModel.uiState.value.formError).isEqualTo(expectedFormError)

            coVerify(exactly = 0) { createSupplierUseCase(any()) }
            coVerify(exactly = 0) { editSupplierUseCase(any(), any()) }
        }
    }

    @Test
    fun `when submitForm is called with 'another' invalid form data, formError should be updated`() {
        runTest {
            // Arrange
            val invalidData = CreateSupplierFormData(
                companyName = "Company name should not exceed 30 characters",
                items = listOf(
                    CreateSupplierFormData.Item(
                        itemName = "Item Name",
                        itemSku = emptyList()
                    )
                )
            )

            val expectedFormError = CreateSupplierFormError(
                companyName = "Max. 30 characters",
                itemName = listOf(
                    null,
                ),
                itemSku = listOf(
                    "You must pick a SKU for this item",
                )
            )

            // Act
            callback.onUpdateFormData(invalidData)
            callback.onSubmitForm()

            // Assert
            assertThat(viewModel.uiState.value.formError).isEqualTo(expectedFormError)
        }
    }

    @Test
    fun `when submitForm is called with valid data and creation returns success, isLoadingOverlay should be false and submitState should be true`() =
        runTest {
            // Arrange
            val emptyFormData = CreateSupplierFormData()
            val validData = CreateSupplierFormData(
                companyName = "companyName",
                items = listOf(
                    CreateSupplierFormData.Item(
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                zipCode = "zipCode",
                companyLocation = "companyAddress",
                companyPhoneNumber = "1234567890",
                picName = "picNAME",
                picPhoneNumber = "1234567890",
                picEmail = "picEmail@gmail.com"
            )

            val body = CreateUpdateSupplierBody(
                companyName = "companyName",
                item = listOf(
                    CreateUpdateSupplierBody.Item(
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                zipCode = "zipCode",
                companyLocation = "companyAddress",
                companyPhoneNumber = "1234567890",
                picName = "picNAME",
                picPhoneNumber = "1234567890",
                picEmail = "picEmail@gmail.com"
            )

            arrangeCreateSupplierUseCase(
                body = body,
                result = Result.Success(Unit)
            )

            // Act & Assert
            viewModel.uiState.test {
                // Initial State
                val initialFormState = awaitItem()
                assertThat(initialFormState.formData).isEqualTo(emptyFormData)

                // Call Update Form Data
                callback.onUpdateFormData(validData)

                // Consume onUpdateFormData changes
                val updateFormState = awaitItem()
                assertThat(updateFormState.formData).isEqualTo(validData)

                // Call Submit Form
                callback.onSubmitForm()

                // Consume initial submit state
                val initialState = awaitItem()
                assertThat(initialState.isLoadingOverlay).isFalse()
                assertThat(initialState.submitState).isNull()

                // Consume loading state
                val loadingState = awaitItem()
                assertThat(loadingState.isLoadingOverlay).isTrue()
                assertThat(loadingState.submitState).isNull()

                // Consume final state
                val finalState = awaitItem()
                assertThat(finalState.isLoadingOverlay).isFalse()
                assertThat(finalState.submitState).isTrue()

//            // Test Fail
//            val testFail = awaitItem()
//            assertThat(testFail.isLoadingOverlay).isTrue()
//            assertThat(testFail.submitState).isNull()
            }
        }

    @Test
    fun `when submitForm is called with valid data and creation returns success while also stayInForm, call clearField()`() {
        runTest {
            // Arrange
            val validData = CreateSupplierFormData(
                companyName = "companyName",
                items = listOf(
                    CreateSupplierFormData.Item(
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                zipCode = "zipCode",
                companyLocation = "companyAddress",
                companyPhoneNumber = "1234567890",
                picName = "picNAME",
                picPhoneNumber = "1234567890",
                picEmail = "picEmail@gmail.com"
            )

            val body = CreateUpdateSupplierBody(
                companyName = "companyName",
                item = listOf(
                    CreateUpdateSupplierBody.Item(
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                zipCode = "zipCode",
                companyLocation = "companyAddress",
                companyPhoneNumber = "1234567890",
                picName = "picNAME",
                picPhoneNumber = "1234567890",
                picEmail = "picEmail@gmail.com"
            )

            val emptyFormData = CreateSupplierFormData()
            val emptyFormError = CreateSupplierFormError()

            arrangeCreateSupplierUseCase(
                body = body,
                result = Result.Success(Unit)
            )

            viewModel.uiState.test {
                // Consume initial state
                assertThat(awaitItem().isStayOnForm).isFalse()

                // Set isStayOnForm to true
                callback.onUpdateStayOnForm()

                // Check if isStayOnForm is updated to true
                val stayOnFormState = awaitItem()
                assertThat(stayOnFormState.isStayOnForm).isTrue()
                assertThat(stayOnFormState.formData).isEqualTo(emptyFormData)

                // Call Update Form Data
                callback.onUpdateFormData(validData)

                // Consume onUpdateFormData changes
                val updateFormState = awaitItem()
                assertThat(updateFormState.formData).isEqualTo(validData)

                // Call Submit Form
                callback.onSubmitForm()

                // Consume initial submit state
                val initialState = awaitItem()
                assertThat(initialState.isLoadingOverlay).isFalse()
                assertThat(initialState.submitState).isNull()

                // Consume loading state
                val loadingState = awaitItem()
                assertThat(loadingState.isLoadingOverlay).isTrue()
                assertThat(loadingState.submitState).isNull()

                // Consume success state
                val successState = awaitItem()
                assertThat(successState.isLoadingOverlay).isFalse()
                assertThat(successState.submitState).isTrue()

                // Consume clearField state
                val clearState = awaitItem()
                assertThat(clearState.formData).isEqualTo(emptyFormData)
                assertThat(clearState.formError).isEqualTo(emptyFormError)
                assertThat(clearState.submitState).isNull()

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when submitForm is called and edition returns success, isLoadingOverlay should be false and submitState should be true`() {
        runTest {
            // Arrange
            val id = "id"
            val validData = CreateSupplierFormData(
                companyName = "companyName",
                items = listOf(
                    CreateSupplierFormData.Item(
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                zipCode = "zipCode",
                companyLocation = "companyAddress",
                companyPhoneNumber = "1234567890",
                picName = "picNAME",
                picPhoneNumber = "1234567890",
                picEmail = "picEmail@gmail.com"
            )

            val body = CreateUpdateSupplierBody(
                companyName = "companyName",
                item = listOf(
                    CreateUpdateSupplierBody.Item(
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                zipCode = "zipCode",
                companyLocation = "companyAddress",
                companyPhoneNumber = "1234567890",
                picName = "picNAME",
                picPhoneNumber = "1234567890",
                picEmail = "picEmail@gmail.com"
            )


            arrangeGetSupplierByIdUseCase(
                id = id,
                result = Result.Success(SupplierEntity())
            )

            arrangeEditSupplierUseCase(
                id = id,
                body = body,
                result = Result.Success(Unit)
            )

            viewModel.uiState.test {
                val initialState = awaitItem()
                assertThat(initialState.isLoadingOverlay).isFalse()
                assertThat(initialState.isEditForm).isFalse()

                viewModel.getSupplyById(id)

                val getSupplyByIdState = awaitItem()
                assertThat(getSupplyByIdState.isLoadingOverlay).isTrue()

                val finalGetSupplyByIdState = awaitItem()
                assertThat(finalGetSupplyByIdState.isLoadingOverlay).isFalse()
                assertThat(finalGetSupplyByIdState.assetId).isEqualTo(id)
                assertThat(finalGetSupplyByIdState.isEditForm).isTrue()

                callback.onUpdateFormData(validData)

                val updateFormState = awaitItem()
                assertThat(updateFormState.formData).isEqualTo(validData)

                callback.onSubmitForm()

                val initialSubmitState = awaitItem()
                assertThat(initialSubmitState.isLoadingOverlay).isFalse()
                assertThat(initialSubmitState.submitState).isNull()

                val loadingSubmitState = awaitItem()
                assertThat(loadingSubmitState.isLoadingOverlay).isTrue()

                val finalState = awaitItem()
                assertThat(finalState.isLoadingOverlay).isFalse()
                assertThat(finalState.submitState).isTrue()
            }
        }
    }

    @Test
    fun `when submitForm is called with valid data but returns error, isLoadingOverlay should be false and submitFalse should be false`() {
        runTest {
            // Arrange
            val emptyFormData = CreateSupplierFormData()
            val validData = CreateSupplierFormData(
                companyName = "companyName",
                items = listOf(
                    CreateSupplierFormData.Item(
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                zipCode = "zipCode",
                companyLocation = "companyAddress",
                companyPhoneNumber = "1234567890",
                picName = "picNAME",
                picPhoneNumber = "1234567890",
                picEmail = "picEmail@gmail.com"
            )

            val body = CreateUpdateSupplierBody(
                companyName = "companyName",
                item = listOf(
                    CreateUpdateSupplierBody.Item(
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                zipCode = "zipCode",
                companyLocation = "companyAddress",
                companyPhoneNumber = "1234567890",
                picName = "picNAME",
                picPhoneNumber = "1234567890",
                picEmail = "picEmail@gmail.com"
            )

            arrangeCreateSupplierUseCase(
                body = body,
                result = Result.Error(Constant.UNEXPECTED_ERROR)
            )

            // Act & Assert
            viewModel.uiState.test {
                // Initial State
                val initialFormState = awaitItem()
                assertThat(initialFormState.formData).isEqualTo(emptyFormData)

                // Call Update Form Data
                callback.onUpdateFormData(validData)

                // Consume onUpdateFormData changes
                val updateFormState = awaitItem()
                assertThat(updateFormState.formData).isEqualTo(validData)

                // Call Submit Form
                callback.onSubmitForm()

                // Consume initial submit state
                val initialState = awaitItem()
                assertThat(initialState.isLoadingOverlay).isFalse()
                assertThat(initialState.submitState).isNull()

                // Consume loading state
                val loadingState = awaitItem()
                assertThat(loadingState.isLoadingOverlay).isTrue()

                // Consume final state
                val finalState = awaitItem()
                assertThat(finalState.isLoadingOverlay).isFalse()
                assertThat(finalState.submitState).isFalse()
            }
        }
    }

    // Add Supplier Item Button Test
    @Test
    fun `when onAddSupplierItem is called, add a new item to the item list`() {
        runTest {
            // Initial State
            assertThat(viewModel.uiState.value.formData.items).hasSize(1)

            // Arrange
            val item = CreateSupplierFormData.Item()

            // Act
            callback.onAddSupplierItem()

            // Assert
            assertThat(viewModel.uiState.value.formData.items).hasSize(2)
            assertThat(viewModel.uiState.value.formData.items[0]).isEqualTo(item)
            assertThat(viewModel.uiState.value.formData.items[1]).isEqualTo(item)
        }
    }

    // Remove Supplier Item Button Test
    @Test
    fun `when onRemoveSupplierItem is called, remove an item to the item list`() {
        runTest {
            // Initial State
            val item = CreateSupplierFormData.Item()

            assertThat(viewModel.uiState.value.formData.items).hasSize(1)
            assertThat(viewModel.uiState.value.formData.items[0]).isEqualTo(item)


            // Act
            callback.onRemoveSupplierItem(item)

            // Assert
            assertThat(viewModel.uiState.value.formData.items).hasSize(0)
        }
    }
}