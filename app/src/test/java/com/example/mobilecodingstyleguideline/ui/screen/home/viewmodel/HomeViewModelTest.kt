package com.example.mobilecodingstyleguideline.ui.screen.home.viewmodel

import app.cash.turbine.test
import com.example.apiservices.base.Result
import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.model.SupplierOptionEntity
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.domain.supplier.DeleteSupplierUseCase
import com.example.apiservices.domain.supplier.GetSupplierOptionUseCase
import com.example.apiservices.domain.supplier.GetSuppliersUseCase
import com.example.mobilecodingstyleguideline.MainDispatcherRule
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.model.home.HomeFilterData
import com.example.mobilecodingstyleguideline.model.home.HomeFilterOption
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.tagsamurai.common.model.OptionData
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

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewmodel = HomeViewModel(
            getSuppliersUseCase = getSuppliersUseCase,
            getSupplierOptionUseCase = getSupplierOptionUseCase,
            deleteSupplierUseCase = deleteSupplierUseCase
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
    fun `when getSupplierUseCase error isLoading should be false and supplierList should be empty object`() =
        runTest {
            arrangeGetSupplierUseCase(GetSupplierQueryParams(), Result.Error("error"))

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
                statusOption = listOf(
                    OptionData(
                        "Active",
                        true
                    ),
                    OptionData(
                        "Inactive",
                        false
                    )
                ),
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

            val optionQueryParams = GetSupplierOptionQueryParams(
                supplierOption = true,
                cityOption = true,
                itemNameOption = true,
                modifiedByOption = true
            )

            arrangeGetSupplierOptionUseCase(optionQueryParams, Result.Success(result))

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

            val optionQueryParams = GetSupplierOptionQueryParams(
                supplierOption = true,
                cityOption = true,
                itemNameOption = true,
                modifiedByOption = true
            )

            arrangeGetSupplierOptionUseCase(optionQueryParams, Result.Error("error"))

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

            callback.onDeleteSuppliers(listOf(id))
            assertThat(viewmodel.uiState.value.isLoadingOverlay).isFalse()
            assertThat(viewmodel.uiState.value.deleteState).isTrue()


//            viewmodel.uiState.test {
//                assertThat(awaitItem().isLoadingOverlay).isFalse()
//
//                // Act: Perform the deletion
//                callback.onDeleteSuppliers(listOf(id))
//
//                // Assert #1: The overlay state becomes true
//                assertThat(awaitItem().isLoadingOverlay).isTrue()
//
//                // Assert #2: The overlay state becomes false and deleteState is true
//                val finalDeleteState = awaitItem()
//                assertThat(finalDeleteState.isLoadingOverlay).isFalse()
//                assertThat(finalDeleteState.deleteState).isTrue()
//
//                // Assert #3: The isLoading state becomes true from initSupplier()
//                assertThat(awaitItem().isLoading).isTrue()
//
//                // Assert #4: The isLoading state becomes false from initSupplier()
//                val finalInitState = awaitItem()
//                assertThat(finalInitState.isLoading).isFalse()
//
//                // Consume any other events to avoid the error.
//                cancelAndIgnoreRemainingEvents()
//            }
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

//    // Search Test
//    @Test
//    fun `when `() = {
//        runTest {
//            TODO("Search Test")
//        }
//    }
//
//    // Update Asset
//    @Test
//    fun `when `() =
//        runTest {
//            TODO("Update Asset Test")
//        }
//
//    // ToggleSelectAll Test
//    @Test
//    fun `when `() =
//        runTest {
//            TODO("ToggleSelectAll Test")
//        }
//
//    // UpdateFilter Test
//    @Test
//    fun `when `() =
//        runTest {
//            TODO("UpdateFilter Test")
//        }
//
//    // ResetMessageState Test
//    @Test
//    fun `when `() =
//        runTest {
//            TODO("ResetMessageState Test")
//        }
//
//    // OnUpdateAsset Test
//    @Test
//    fun `when  `() =
//        runTest {
//            TODO("OnUpdateAsset Test")
//        }
//
//    // Status Update Test
//    @Test
//    fun `when `() =
//        runTest {
//            TODO("StatusUpdate Test")
//        }
}