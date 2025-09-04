package com.example.mobilecodingstyleguideline.ui.screen.home.viewmodel

import app.cash.turbine.test
import com.example.apiservices.base.Result
import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.model.SupplierOptionEntity
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.apiservices.domain.supplier.DeleteSupplierUseCase
import com.example.apiservices.domain.supplier.EditStatusSupplierUseCase
import com.example.apiservices.domain.supplier.GetSupplierOptionUseCase
import com.example.apiservices.domain.supplier.GetSuppliersUseCase
import com.example.apiservices.util.Constant
import com.example.mobilecodingstyleguideline.MainDispatcherRule
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.model.home.HomeFilterData
import com.example.mobilecodingstyleguideline.model.home.HomeFilterOption
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.tagsamurai.common.model.OptionData
import com.tagsamurai.tscomponents.utils.Utils
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    private lateinit var viewmodel: HomeViewModel

    private lateinit var callback: HomeCallback

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @MockK
    private lateinit var getSuppliersUseCase: GetSuppliersUseCase

    @MockK
    private lateinit var getSupplierOptionUseCase: GetSupplierOptionUseCase

    @MockK
    private lateinit var deleteSupplierUseCase: DeleteSupplierUseCase

    @MockK
    private lateinit var editStatusSupplierUseCase: EditStatusSupplierUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewmodel = HomeViewModel(
            getSuppliersUseCase = getSuppliersUseCase,
            getSupplierOptionUseCase = getSupplierOptionUseCase,
            deleteSupplierUseCase = deleteSupplierUseCase,
            editStatusSupplierBody = editStatusSupplierUseCase
        )

        callback = viewmodel.getCallback()
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
            getSuppliersUseCase(params)
        } returns flowOf(result)
    }

    private fun arrangeGetSupplierOptionUseCase(
        params: GetSupplierOptionQueryParams,
        result: Result<SupplierOptionEntity>
    ) {
        coEvery {
            getSupplierOptionUseCase(params)
        } returns flowOf(result)
    }

    private fun arrangeDeleteSupplierUseCase(
        body: DeleteSupplierBody,
        result: Result<Unit>
    ) {
        coEvery {
            deleteSupplierUseCase(body)
        } returns flowOf(result)
    }

    private fun arrangeEditStatusUseCase(
        body: PatchEditStatusSupplierBody,
        result: Result<Unit>
    ) {
        coEvery {
            editStatusSupplierUseCase(body)
        } returns flowOf(result)
    }

    // getCallback Test
    @Test
    fun `HomeCallback should trigger correct callbacks`() =
        runTest {
            var onRefresh = false
            var onSearch = false
            var onFilter = false
            var onToggleSelectAll = false
            var onUpdateSelectedItem = false
            var onDeleteSupplier = false
            var onResetMessageState = false

            val callback = HomeCallback(
                onRefresh = { onRefresh = true },
                onFilter = { onFilter = true },
                onSearch = { onSearch = true },
                onUpdateItemSelected = { onUpdateSelectedItem = true },
                onToggleSelectAll = { onToggleSelectAll = true },
                onDeleteSuppliers = { onDeleteSupplier = true },
                onResetMessageState = { onResetMessageState = true },
            )

            // Act
            callback.onRefresh()
            callback.onSearch("testQuery")
            callback.onFilter(HomeFilterData())
            callback.onUpdateItemSelected(SupplierEntity())
            callback.onToggleSelectAll()
            callback.onDeleteSuppliers(listOf())
            callback.onResetMessageState()

            // Assert
            Truth.assertThat(onRefresh).isTrue()
            Truth.assertThat(onSearch).isTrue()
            Truth.assertThat(onFilter).isTrue()
            Truth.assertThat(onToggleSelectAll).isTrue()
            Truth.assertThat(onUpdateSelectedItem).isTrue()
            Truth.assertThat(onDeleteSupplier).isTrue()
            Truth.assertThat(onResetMessageState).isTrue()
        }

    // OnRefresh Test
    @Test
    fun `when onRefresh, search query must be empty, filter data must be empty, and show search must be false`() {
        runTest {
            // Arrange
            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Success(listOf())
            )
            arrangeGetSupplierOptionUseCase(
                params = GetSupplierOptionQueryParams(),
                result = Result.Success(SupplierOptionEntity())
            )

            // Act
            callback.onRefresh()

            // Assert
            assertThat(viewmodel.uiState.value.searchQuery).isEmpty()
            assertThat(viewmodel.uiState.value.filterData).isEqualTo(HomeFilterData())
            assertThat(viewmodel.uiState.value.showSearch).isFalse()
        }
    }

    // InitSupplier Test
    @Test
    fun `when getSupplierUseCase success isLoading should be false and supplierList should return correct object`() =
        runTest {
            val expected = listOf(
                SupplierEntity(
                    id = "id",
                    status = true,
                    companyName = "name",
                    item = listOf(),
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

            arrangeGetSupplierUseCase(GetSupplierQueryParams(), Result.Success(expected))

            arrangeGetSupplierOptionUseCase(
                params = GetSupplierOptionQueryParams(),
                result = Result.Success(SupplierOptionEntity())
            )

            viewmodel.uiState.test {
                assertThat(awaitItem().isLoading).isFalse()

                viewmodel.initSupplier()

                assertThat(awaitItem().isLoading).isTrue()

                val finalState = awaitItem()
                assertThat(finalState.isLoading).isFalse()
                assertThat(finalState.supplier).isEqualTo(expected)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when getSupplierUseCase error isLoading should be false and supplierList returns empty object`() =
        runTest {
            arrangeGetSupplierUseCase(GetSupplierQueryParams(), Result.Error("error"))

            arrangeGetSupplierOptionUseCase(
                params = GetSupplierOptionQueryParams(),
                result = Result.Success(SupplierOptionEntity())
            )

            viewmodel.uiState.test {
                assertThat(awaitItem().isLoading).isFalse()

                viewmodel.initSupplier()

                assertThat(awaitItem().isLoading).isTrue()

                val finalState = awaitItem()
                assertThat(finalState.isLoading).isFalse()
                assertThat(finalState.supplier).isEmpty()

                cancelAndIgnoreRemainingEvents()
            }
        }

    // GetFilterOption Test
    @Test
    fun `when getSupplierOptionUseCase success isLoadingGroup should be false and filterOption should return correct object`() =
        runTest {
            val expected = HomeFilterOption(
                supplierOption = listOf(
                    OptionData(
                        label = "label",
                        value = "value"
                    )
                ),
                cityOption = listOf(
                    OptionData(
                        label = "label",
                        value = "value"
                    )
                ),
                itemNameOption = listOf(
                    OptionData(
                        label = "label",
                        value = "value"
                    )
                ),
                modifiedByOption = listOf(
                    OptionData(
                        label = "label",
                        value = "value"
                    )
                )
            )

            val result = SupplierOptionEntity(
                statusOption = listOf(
                    SupplierOptionEntity.OptionData(
                        label = "label",
                        value = "value"
                    )
                ),
                supplierOption = listOf(
                    SupplierOptionEntity.OptionData(
                        label = "label",
                        value = "value"
                    )
                ),
                cityOption = listOf(
                    SupplierOptionEntity.OptionData(
                        label = "label",
                        value = "value"
                    )
                ),
                itemNameOption = listOf(
                    SupplierOptionEntity.OptionData(
                        label = "label",
                        value = "value"
                    )
                ),
                modifiedByOption = listOf(
                    SupplierOptionEntity.OptionData(
                        label = "label",
                        value = "value"
                    )
                )
            )

            arrangeGetSupplierOptionUseCase(GetSupplierOptionQueryParams(), Result.Success(result))

            viewmodel.uiState.test {
                assertThat(awaitItem().isLoadingGroup).isFalse()

                viewmodel.getFilterOption()

                assertThat(awaitItem().isLoadingGroup).isTrue()

                val finalState = awaitItem()
                assertThat(finalState.isLoadingGroup).isFalse()
                assertThat(finalState.filterOption).isEqualTo(expected)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when getSupplierOptionUseCase success isLoadingGroup should be false and filterOption should be empty object`() =
        runTest {
            val expected = HomeFilterOption(
                statusOption = listOf(),
                supplierOption = listOf(),
                cityOption = listOf(),
                itemNameOption = listOf(),
                modifiedByOption = listOf()
            )

            arrangeGetSupplierOptionUseCase(GetSupplierOptionQueryParams(), Result.Error("error"))

            viewmodel.uiState.test {
                assertThat(awaitItem().isLoadingGroup).isFalse()

                viewmodel.getFilterOption()

                assertThat(awaitItem().isLoadingGroup).isTrue()

                val finalState = awaitItem()
                assertThat(finalState.isLoadingGroup).isFalse()
                assertThat(finalState.filterOption).isEqualTo(expected)

                cancelAndIgnoreRemainingEvents()
            }
        }

    // OnSearch Test
    @Test
    fun `when onSearch is called, supplier must be updated`() {
        // Arrange
        val searchQuery = "query"

        val queryParams = GetSupplierQueryParams(
            search = searchQuery,
        )

        val filterParams = HomeFilterOption(
            supplierOption = listOf(),
            cityOption = listOf(),
            itemNameOption = listOf(),
            modifiedByOption = listOf()
        )

        arrangeGetSupplierUseCase(
            params = queryParams,
            result = Result.Success(listOf())
        )

        arrangeGetSupplierOptionUseCase(
            params = GetSupplierOptionQueryParams(),
            result = Result.Success(SupplierOptionEntity())
        )

        val expected = HomeUiState(
            searchQuery = searchQuery,
            filterOption = filterParams
        )

        // Act
        callback.onSearch(searchQuery)

        // Assert
        assertThat(viewmodel.uiState.value).isEqualTo(expected)
        assertThat(viewmodel.uiState.value.supplier).isEqualTo(emptyList<SupplierEntity>())
    }

    // ShowSearch Test
    @Test
    fun `when onShowSearch is called, showSearch value should be updated`() {
        runTest {
            // Act
            callback.onShowSearch(true)

            // Assert
            assertThat(viewmodel.uiState.value.showSearch).isTrue()
        }
    }

    // OnUpdateItemSelected Test
    @Test
    fun `when updateItemSelected is called and the item is not selected, item should be added to itemSelected`() {
        runTest {
            val supplier = listOf(
                SupplierEntity(
                    id = "id",
                    companyName = "companyName",
                    item = listOf(
                        SupplierEntity.Item(
                            id = "itemId",
                            supplierId = "supplierId",
                            itemName = "itemName",
                            itemSku = listOf("sku")
                        )
                    ),
                    country = "country",
                    state = "state",
                    city = "city",
                    companyPhoneNumber = "123",
                    companyLocation = "companyLocation",
                    zipCode = "zipCode",
                    picName = "picName",
                    picPhoneNumber = "123",
                    picEmail = "email@gmail.com",
                    status = true,
                    modifiedBy = "modifiedBy",
                    updatedAt = "updatedAt",
                    createdAt = "createdAt"
                )
            )

            val expected = HomeUiState(
                itemSelected = supplier,
                supplierDefault = supplier,
                supplier = supplier
            )

            arrangeGetSupplierUseCase(GetSupplierQueryParams(), Result.Success(supplier))

            arrangeGetSupplierOptionUseCase(
                GetSupplierOptionQueryParams(),
                Result.Success(SupplierOptionEntity())
            )

            // Act
            viewmodel.initSupplier()
            callback.onUpdateItemSelected(supplier[0])

            // Assert
            assertThat(viewmodel.uiState.value).isEqualTo(expected)
        }
    }

    @Test
    fun `when updateItemSelected is called and the item is selected, item should be removed from itemSelected`() {
        runTest {
            val supplier = listOf(
                SupplierEntity(
                    id = "id",
                    companyName = "companyName",
                    item = listOf(
                        SupplierEntity.Item(
                            id = "itemId",
                            supplierId = "supplierId",
                            itemName = "itemName",
                            itemSku = listOf("sku")
                        )
                    ),
                    country = "country",
                    state = "state",
                    city = "city",
                    companyPhoneNumber = "123",
                    companyLocation = "companyLocation",
                    zipCode = "zipCode",
                    picName = "picName",
                    picPhoneNumber = "123",
                    picEmail = "email@gmail.com",
                    status = true,
                    modifiedBy = "modifiedBy",
                    updatedAt = "updatedAt",
                    createdAt = "createdAt"
                )
            )

            val expected = HomeUiState(
                itemSelected = emptyList(),
                supplierDefault = supplier,
                supplier = supplier
            )

            arrangeGetSupplierUseCase(GetSupplierQueryParams(), Result.Success(supplier))

            arrangeGetSupplierOptionUseCase(
                GetSupplierOptionQueryParams(),
                Result.Success(SupplierOptionEntity())
            )

            // Act
            viewmodel.initSupplier()
            callback.onUpdateItemSelected(supplier[0])
            callback.onUpdateItemSelected(supplier[0])

            // Assert
            assertThat(viewmodel.uiState.value).isEqualTo(expected)
        }
    }

    // ToggleSelectAll Test
    @Test
    fun `when toggleSelectAll is called and isAllSelected is false, add all supplier to itemSelected and isAllSelected must be true`() {
        // Arrange
        val supplier = listOf(
            SupplierEntity(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    SupplierEntity.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                companyPhoneNumber = "123",
                companyLocation = "companyLocation",
                zipCode = "zipCode",
                picName = "picName",
                picPhoneNumber = "123",
                picEmail = "email@gmail.com",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )
        )

        arrangeGetSupplierUseCase(
            params = GetSupplierQueryParams(),
            result = Result.Success(supplier)
        )

        arrangeGetSupplierOptionUseCase(
            params = GetSupplierOptionQueryParams(),
            result = Result.Success(SupplierOptionEntity())
        )

        // Act
        viewmodel.initSupplier()
        callback.onToggleSelectAll()

        // Assert
        assertThat(viewmodel.uiState.value.itemSelected).isEqualTo(supplier)
        assertThat(viewmodel.uiState.value.isAllSelected).isTrue()
    }

    @Test
    fun `when toggleSelectAll is called and isAllSelected is true, remove all supplier from itemSelected and isAllSelected must be false`() {
        // Arrange
        val supplier = listOf(
            SupplierEntity(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    SupplierEntity.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                companyPhoneNumber = "123",
                companyLocation = "companyLocation",
                zipCode = "zipCode",
                picName = "picName",
                picPhoneNumber = "123",
                picEmail = "email@gmail.com",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )
        )

        arrangeGetSupplierUseCase(
            params = GetSupplierQueryParams(),
            result = Result.Success(supplier)
        )

        arrangeGetSupplierOptionUseCase(
            params = GetSupplierOptionQueryParams(),
            result = Result.Success(SupplierOptionEntity())
        )

        // Act
        viewmodel.initSupplier()
        callback.onToggleSelectAll()
        callback.onToggleSelectAll()

        // Assert
        assertThat(viewmodel.uiState.value.itemSelected).isEmpty()
        assertThat(viewmodel.uiState.value.isAllSelected).isFalse()
    }

    // UpdateFilter Test
    @Test
    fun `when updateFilter is called, returns filtered supplier list`() {
        runTest {
            // Arrange
            val filterData = HomeFilterData(
                supplierSelected = listOf("supplierOption"),
                citySelected = listOf("cityOption"),
                itemSelected = listOf("itemOption"),
                modifierSelected = listOf("modifierOption")
            )

            val queryParams = GetSupplierQueryParams(
                supplier = Utils.toJsonIfNotEmpty(filterData.supplierSelected),
                city = Utils.toJsonIfNotEmpty(filterData.citySelected),
                itemName = Utils.toJsonIfNotEmpty(filterData.itemSelected),
                modifiedBy = Utils.toJsonIfNotEmpty(filterData.modifierSelected),
            )

            val filterOption = SupplierOptionEntity(
                supplierOption = listOf(SupplierOptionEntity.OptionData()),
                cityOption = listOf(SupplierOptionEntity.OptionData()),
                itemNameOption = listOf(SupplierOptionEntity.OptionData()),
                modifiedByOption = listOf(SupplierOptionEntity.OptionData())
            )

            arrangeGetSupplierOptionUseCase(
                params = GetSupplierOptionQueryParams(),
                result = Result.Success(filterOption)
            )

            arrangeGetSupplierUseCase(
                params = queryParams,
                result = Result.Success(listOf())
            )

            // Act
            callback.onFilter(filterData)

            // Assert
            assertThat(viewmodel.uiState.value.filterData).isEqualTo(filterData)
            assertThat(viewmodel.uiState.value.supplier).isEmpty()
        }
    }

    // DeleteSupplier Test
    @Test
    fun `when deleteSupplierUseCase isLoadingOverlay should be false and deleteState should be true`() =
        runTest {
            val id = "id"
            arrangeDeleteSupplierUseCase(
                body = DeleteSupplierBody(listOf(id)),
                result = Result.Success(Unit)
            )

            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Success(listOf())
            )

            arrangeGetSupplierOptionUseCase(
                params = GetSupplierOptionQueryParams(),
                result = Result.Success(SupplierOptionEntity())
            )

            callback.onDeleteSuppliers(listOf(id))
            assertThat(viewmodel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewmodel.uiState.value.deleteState).isTrue()
        }

    @Test
    fun `when deleteSupplierUseCase error isLoadingOverlay should be false and deleteState should be false`() =
        runTest {
            // Arrange
            val id = "id"
            arrangeDeleteSupplierUseCase(
                body = DeleteSupplierBody(listOf(id)),
                result = Result.Error("error")
            )
            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Error("error")
            )

            // Act
            callback.onDeleteSuppliers(listOf(id))

            // Assert
            assertThat(viewmodel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewmodel.uiState.value.deleteState).isFalse()
        }

    // onResetMessageState Test
    @Test
    fun `when resetMessageState is called, both delete state and status state should be null`() {
        runTest {
            // Act
            callback.onResetMessageState()

            // Assert
            assertThat(viewmodel.uiState.value.deleteState).isNull()
            assertThat(viewmodel.uiState.value.statusState).isNull()
        }
    }

    // Edit Status Test
    @Test
    fun `when editStatusSupplierUseCase returns success, isLoadingOverlay should be false and statusState should be true`() {
        runTest {
            // Arrange
            val id = listOf("id")
            val newStatus = true
            val body = PatchEditStatusSupplierBody(
                supplierID = id,
                status = newStatus
            )
            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Success(listOf())
            )

            arrangeGetSupplierOptionUseCase(
                params = GetSupplierOptionQueryParams(),
                result = Result.Success(SupplierOptionEntity())
            )

            arrangeEditStatusUseCase(
                body = body,
                result = Result.Success(Unit)
            )

            // Act
            callback.onEditStatusSupplier(id, newStatus)

            // Assert
            assertThat(viewmodel.uiState.value.statusState).isTrue()
            assertThat(viewmodel.uiState.value.isLoadingOverlay).isFalse()
        }
    }

    @Test
    fun `when editStatusSupplierUseCase returns success and has supplier matching id, isLoadingOverlay should be false and update supplier status`() {
        runTest {
            // Arrange
            val id = listOf("id")
            val newStatus = true
            val body = PatchEditStatusSupplierBody(
                supplierID = id,
                status = newStatus
            )

            val initial = listOf(
                SupplierEntity(
                    id = "id",
                    status = false,
                    companyName = "name",
                    item = listOf(),
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

            val expected = listOf(
                SupplierEntity(
                    id = "id",
                    status = true,
                    companyName = "name",
                    item = listOf(),
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

            coEvery {
                getSuppliersUseCase(GetSupplierQueryParams())
            } returns flowOf(Result.Success(initial))

            arrangeGetSupplierOptionUseCase(
                params = GetSupplierOptionQueryParams(),
                result = Result.Success(SupplierOptionEntity())
            )

            arrangeEditStatusUseCase(
                body = body,
                result = Result.Success(Unit)
            )

            // Act
            viewmodel.initSupplier()
            callback.onEditStatusSupplier(id, newStatus)

            // Assert
            assertThat(viewmodel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewmodel.uiState.value.isActive).isTrue()
            assertThat(viewmodel.uiState.value.statusState).isTrue()
            assertThat(viewmodel.uiState.value.supplier).isEqualTo(expected)
        }
    }

    @Test
    fun `when editStatusSupplierUseCase returns success but has no supplier matching id, isLoadingOverlay should be false and supplier does not change`() {
        runTest {
            // Arrange
            val id = listOf("notId")
            val newStatus = false
            val body = PatchEditStatusSupplierBody(
                supplierID = id,
                status = newStatus
            )
            val supplier = listOf(
                SupplierEntity(
                    id = "id",
                    status = true,
                    companyName = "name",
                    item = listOf(),
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

            val expected = supplier

            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Success(supplier)
            )

            arrangeGetSupplierOptionUseCase(
                params = GetSupplierOptionQueryParams(),
                result = Result.Success(SupplierOptionEntity())
            )

            arrangeEditStatusUseCase(
                body = body,
                result = Result.Success(Unit)
            )

            // Act
            viewmodel.initSupplier()
            callback.onEditStatusSupplier(id, newStatus)

            // Assert
            assertThat(viewmodel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewmodel.uiState.value.isActive).isFalse()
            assertThat(viewmodel.uiState.value.statusState).isTrue()
            assertThat(viewmodel.uiState.value.supplier).isEqualTo(expected)
        }
    }

    @Test
    fun `when editStatusSupplierUseCase returns error, isLoadingOverlay should be false, and statusState should be false`() {
        runTest {
            // Arrange
            val id = listOf("id")
            val newStatus = true
            val body = PatchEditStatusSupplierBody(
                supplierID = id,
                status = newStatus
            )

            val expectedErrorMsg = Constant.UNEXPECTED_ERROR
            arrangeGetSupplierUseCase(
                params = GetSupplierQueryParams(),
                result = Result.Success(listOf())
            )

            arrangeGetSupplierOptionUseCase(
                params = GetSupplierOptionQueryParams(),
                result = Result.Success(SupplierOptionEntity())
            )

            arrangeEditStatusUseCase(
                body = body,
                result = Result.Error(expectedErrorMsg)
            )

            // Act
            callback.onEditStatusSupplier(id, newStatus)

            // Assert
            assertThat(viewmodel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewmodel.uiState.value.statusState).isFalse()
        }
    }
}