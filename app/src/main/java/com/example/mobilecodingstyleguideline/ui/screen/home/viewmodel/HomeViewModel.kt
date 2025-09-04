package com.example.mobilecodingstyleguideline.ui.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiservices.base.Result
import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.apiservices.domain.supplier.DeleteSupplierUseCase
import com.example.apiservices.domain.supplier.EditStatusSupplierUseCase
import com.example.apiservices.domain.supplier.GetSupplierOptionUseCase
import com.example.apiservices.domain.supplier.GetSuppliersUseCase
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.model.home.HomeFilterData
import com.example.mobilecodingstyleguideline.model.home.HomeFilterOption
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.example.mobilecodingstyleguideline.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSuppliersUseCase: GetSuppliersUseCase,
    private val getSupplierOptionUseCase: GetSupplierOptionUseCase,
    private val deleteSupplierUseCase: DeleteSupplierUseCase,
    private val editStatusSupplierBody: EditStatusSupplierUseCase
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
            onUpdateSupplier = ::onUpdateAsset
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
        _uiState.value = _uiState.value.copy(isLoading = true)

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
        _uiState.value = _uiState.value.copy(isLoadingGroup = true)

        getSupplierOptionUseCase(GetSupplierOptionQueryParams()).onEach { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data

                    _uiState.value = _uiState.value.copy(
                        filterOption = HomeFilterOption(
                            supplierOption = Util.convertOptionsData(data.supplierOption),
                            cityOption = Util.convertOptionsData(data.cityOption),
                            itemNameOption = Util.convertOptionsData(data.itemNameOption),
                            modifiedByOption = Util.convertOptionsData(data.modifiedByOption)
                        ),
                        isLoadingGroup = false
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        filterOption = HomeFilterOption(),
                        isLoadingGroup = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun search(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

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
        _uiState.value = _uiState.value.copy(
            itemSelected = if (selectedItems.contains(supply)) {
                selectedItems.apply { remove(supply) }
            } else {
                selectedItems.apply { add(supply) }
            }
        )
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
        _uiState.value = _uiState.value.copy(filterData = data)

        init()
    }

    private fun deleteSuppliers(itemIds: List<String>) {
        _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

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
                    _uiState.value = _uiState.value.copy(
                        isLoadingOverlay = false,
                        deleteState = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun resetMessageState() {
        _uiState.value = _uiState.value.copy(
            deleteState = null,
            statusState = null,
        )
    }

    private fun onUpdateAsset(data: SupplierEntity) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val assets = _uiState.value.supplier.toMutableList()

            if (data.id.isBlank()) {
                val newId = UUID.randomUUID().toString()
                val newAsset = data.copy(id = newId)

                assets.add(index = 0, element = newAsset)
            } else {
                assets.indexOfFirst { it.id == data.id }.apply { assets[this] = data }
            }

            delay(1000)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                supplier = assets,
                supplierDefault = assets
            )
        }
    }

    private fun editStatusSupplier(supplierId: List<String>, newStatus: Boolean) {
        _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

        val body = PatchEditStatusSupplierBody(supplierId, newStatus)
        editStatusSupplierBody.invoke(body).onEach { result ->
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