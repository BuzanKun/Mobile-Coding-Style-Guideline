package com.example.mobilecodingstyleguideline.ui.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiservices.base.Result
import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.domain.supplier.DeleteSupplierUseCase
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
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSuppliersUseCase: GetSuppliersUseCase,
    private val getSupplierOptionUseCase: GetSupplierOptionUseCase,
    private val deleteSupplierUseCase: DeleteSupplierUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun init() {
        initSupplier()
        getFilterOption()
    }

    fun getCallback(): HomeCallback {
        return HomeCallback(
            onRefresh = { initSupplier(isRefresh = true) },
            onFilter = ::updateFilter,
            onSearch = ::search,
            onUpdateItemSelected = ::updateItemSelected,
            onToggleSelectAll = ::toggleSelectAll,
            onDeleteSuppliers = ::deleteSuppliers,
            onResetMessageState = ::resetMessageState,
            onActivateSuppliers = ::activateAssets,
            onInactivateSuppliers = ::inactivateAssets,
            onUpdateSupplier = ::onUpdateAsset
        )
    }

    fun initSupplier(isRefresh: Boolean = false) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        if (isRefresh) {
            _uiState.value = _uiState.value.copy(
                searchQuery = "",
                filterData = HomeFilterData(),
            )
        }

        getSuppliersUseCase(GetSupplierQueryParams()).onEach { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data.map { item ->
                        SupplierEntity(
                            id = item.id,
                            status = item.status,
                            companyName = item.companyName,
                            item = item.item,
                            country = item.country,
                            state = item.state,
                            city = item.city,
                            zipCode = item.zipCode,
                            companyLocation = item.companyLocation,
                            companyPhoneNumber = item.companyPhoneNumber,
                            picName = item.picName,
                            picPhoneNumber = item.picPhoneNumber,
                            picEmail = item.picEmail,
                            updatedAt = item.updatedAt,
                            createdAt = item.createdAt
                        )
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        supplyDefault = data,
                        supplier = data
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getFilterOption() {
        _uiState.value = _uiState.value.copy(isLoadingGroup = true)

        val optionQueryParams = GetSupplierOptionQueryParams(
            supplierOption = true,
            cityOption = true,
            itemNameOption = true,
            modifiedByOption = true
        )

        getSupplierOptionUseCase(optionQueryParams).onEach { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data

                    _uiState.value = _uiState.value.copy(
                        filterOption = HomeFilterOption(
                            statusOption = Util.getStatusOption(),
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
        val query = query.trim()
        _uiState.value = _uiState.value.copy(searchQuery = query)

        val itemsFilter = _uiState.value.supplier
        val items = if (query.isBlank()) {
            itemsFilter
        } else {
            itemsFilter.filter { item ->
                val queryMatch = listOf(
                    item.companyName,
                    item.state,
                    item.city,
                    item.country,
                    item.picName
                ).any {
                    it.contains(query, ignoreCase = true)
                }

                val itemQueryMatch = item.item.any { orderItem ->
                    orderItem.itemName.contains(query, ignoreCase = true)
                }

                queryMatch || itemQueryMatch
            }
        }
        updateAssets(items)
    }

    private fun updateAssets(supplies: List<SupplierEntity>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1000)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                supplier = supplies
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

        val itemsFilter = _uiState.value.supplier
        val items = itemsFilter.filter { item ->
            val isDateInRange = if (data.dateSelected.isEmpty()) {
                true
            } else {
                val startMillis = data.dateSelected.getOrNull(0)
                val endMillis = data.dateSelected.getOrNull(1)?.plus(86399999L)

                val startDate = startMillis?.let {
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
                }
                val endDate = endMillis?.let {
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
                }

                val itemDate = ZonedDateTime.parse(item.updatedAt)

                if (startDate != null && endDate != null && itemDate != null) {
                    !itemDate.isBefore(startDate) && !itemDate.isAfter(endDate)
                } else {
                    true
                }
            }


            (data.activeSelected.isEmpty() || data.activeSelected.contains(item.status)) &&
                    (data.supplierSelected.isEmpty() || data.supplierSelected.contains(item.companyName)) &&
                    (data.citySelected.isEmpty() || data.citySelected.contains(item.city)) &&
                    (data.picSelected.isEmpty() || data.picSelected.contains(item.picName)) &&
                    (data.itemSelected.isEmpty() || item.item.any { orderItem ->
                        data.itemSelected.contains(orderItem.itemName)
                    })
                    && isDateInRange
        }
        updateAssets(items)
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
            activateState = null,
            inactivateState = null
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
                supplyDefault = assets
            )
        }
    }

    private fun activateAssets(items: List<SupplierEntity>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

            val itemsToUpdate = items.map { it.id }.toSet()

            val updatedAssets = _uiState.value.supplier.map { asset ->
                if (itemsToUpdate.contains(asset.id)) {
                    asset.copy(
                        status = true
                    )
                } else {
                    asset
                }
            }

            _uiState.update {
                it.copy(
                    isLoadingOverlay = false,
                    supplier = updatedAssets,
                    supplyDefault = updatedAssets,
                    itemSelected = emptyList(),
                    activateState = true
                )
            }
        }
    }

    private fun inactivateAssets(items: List<SupplierEntity>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

            val itemsToUpdate = items.map { it.id }.toSet()

            val updatedAssets = _uiState.value.supplier.map { asset ->
                if (itemsToUpdate.contains(asset.id)) {
                    asset.copy(
                        status = false
                    )
                } else {
                    asset
                }
            }

            _uiState.update {
                it.copy(
                    isLoadingOverlay = false,
                    supplier = updatedAssets,
                    supplyDefault = updatedAssets,
                    itemSelected = emptyList(),
                    inactivateState = true
                )
            }
        }
    }
}