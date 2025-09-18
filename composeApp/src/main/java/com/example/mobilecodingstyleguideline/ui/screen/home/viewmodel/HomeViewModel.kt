package com.example.mobilecodingstyleguideline.ui.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.model.home.HomeFilterData
import com.example.mobilecodingstyleguideline.model.home.HomeFilterOption
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.example.mobilecodingstyleguideline.util.Util
import com.example.shared.base.Result
import com.example.shared.data.model.SupplierEntity
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.shared.domain.supplier.DeleteSupplierUseCase
import com.example.shared.domain.supplier.EditStatusSupplierUseCase
import com.example.shared.domain.supplier.GetSupplierOptionUseCase
import com.example.shared.domain.supplier.GetSuppliersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val getSuppliersUseCase: GetSuppliersUseCase,
    private val getSupplierOptionUseCase: GetSupplierOptionUseCase,
    private val deleteSupplierUseCase: DeleteSupplierUseCase,
    private val editStatusSupplierUseCase: EditStatusSupplierUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun init() {
        initSupplier()
    }

    fun getCallback(): HomeCallback {
        return HomeCallback(
            onRefresh = ::onRefresh,
            onFilter = ::updateFilter,
            onSearch = ::search,
            onShowSearch = ::showSearch,
            onUpdateItemSelected = ::updateItemSelected,
            onToggleSelectAll = ::toggleSelectAll,
            onDeleteSuppliers = ::deleteSuppliers,
            onResetMessageState = ::resetMessageState,
            onEditStatusSupplier = ::editStatusSupplier,
        )
    }

    fun onRefresh() {
        _uiState.update { currData ->
            currData.copy(
                searchQuery = "",
                filterData = HomeFilterData(),
                showSearch = false
            )
        }
        init()
    }

    fun initSupplier() {
        _uiState.update { it.copy(isLoading = true) }

        getSuppliersUseCase(uiState.value.queryParams).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update { currData ->
                        currData.copy(
                            supplier = result.data,
                            supplierDefault = result.data,
                            isLoading = false
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update { currData ->
                        currData.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)

        getFilterOption()
    }

    fun getFilterOption() {
        _uiState.update { it.copy(isLoadingGroup = true) }

        getSupplierOptionUseCase(GetSupplierOptionQueryParams()).onEach { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data

                    _uiState.update {
                        it.copy(
                            filterOption = HomeFilterOption(
                                supplierOption = Util.convertOptionsData(data.supplierOption),
                                cityOption = Util.convertOptionsData(data.cityOption),
                                itemNameOption = Util.convertOptionsData(data.itemNameOption),
                                modifiedByOption = Util.convertOptionsData(data.modifiedByOption)
                            ),
                            isLoadingGroup = false
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            filterOption = HomeFilterOption(),
                            isLoadingGroup = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun search(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        init()
    }

    fun showSearch(searchStatus: Boolean) {
        _uiState.update { currData ->
            currData.copy(
                showSearch = searchStatus
            )
        }
    }

    private fun updateItemSelected(supply: SupplierEntity) {
        val selectedItems = _uiState.value.itemSelected.toMutableList()
        _uiState.update {
            it.copy(
                itemSelected = if (selectedItems.contains(supply)) {
                    selectedItems.apply { remove(supply) }
                } else {
                    selectedItems.apply { add(supply) }
                }
            )
        }
    }

    private fun toggleSelectAll() {
        _uiState.update {
            it.copy(
                itemSelected = if (it.isAllSelected) {
                    emptyList()
                } else {
                    it.supplier
                },
                isAllSelected = !it.isAllSelected
            )
        }
    }

    private fun updateFilter(data: HomeFilterData) {
        _uiState.update { it.copy(filterData = data) }

        init()
    }

    private fun deleteSuppliers(itemIds: List<String>) {
        _uiState.update { it.copy(isLoadingOverlay = true) }

        val body = DeleteSupplierBody(
            supplierID = itemIds
        )

        deleteSupplierUseCase(body).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update { currData ->
                        currData.copy(
                            isLoadingOverlay = false,
                            itemSelected = emptyList(),
                            deleteState = true
                        )
                    }
                    initSupplier()
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            deleteState = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun resetMessageState() {
        _uiState.update {
            it.copy(
                deleteState = null,
                statusState = null,
            )
        }
    }

    private fun editStatusSupplier(supplierId: List<String>, newStatus: Boolean) {
        _uiState.update { it.copy(isLoadingOverlay = true) }

        val body = PatchEditStatusSupplierBody(supplierId, newStatus)
        editStatusSupplierUseCase.invoke(body).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update { currData ->
                        currData.copy(
                            isLoadingOverlay = false,
                            isActive = newStatus,
                            statusState = true,
                            supplier = _uiState.value.supplier.map { supplier ->
                                if (supplierId.contains(supplier.id)) {
                                    supplier.copy(status = newStatus)
                                } else {
                                    supplier
                                }
                            }
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update { currData ->
                        currData.copy(
                            isLoadingOverlay = false,
                            statusState = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}