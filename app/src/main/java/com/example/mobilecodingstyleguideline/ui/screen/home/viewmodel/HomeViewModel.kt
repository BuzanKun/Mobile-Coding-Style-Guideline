package com.example.mobilecodingstyleguideline.ui.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.model.home.HomeFilterData
import com.example.mobilecodingstyleguideline.model.home.HomeFilterOption
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.example.mobilecodingstyleguideline.util.Asset
import com.example.mobilecodingstyleguideline.util.DataDummy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun init() {
        initAssets()
        getFilterOption()
    }

    private fun getFilterOption() {
        _uiState.value = _uiState.value.copy(
            filterOption = HomeFilterOption(
                activeOption = DataDummy.getActive(),
                supplierOption = DataDummy.generateOptionsDataString(DataDummy.getSupplier()),
                cityOption = DataDummy.generateOptionsDataString(DataDummy.getCity()),
                itemOption = DataDummy.generateOptionsDataString(DataDummy.getItemName()),
                picOption = DataDummy.generateOptionsDataString(DataDummy.getModifiedBy())
            )
        )
    }

    fun getCallback(): HomeCallback {
        return HomeCallback(
            onRefresh = { initAssets(isRefresh = true) },
            onFilter = ::updateFilter,
            onSearch = ::search,
            onUpdateItemSelected = ::updateItemSelected,
            onToggleSelectAll = ::toggleSelectAll,
            onDeleteAssets = ::deleteAssets,
            onResetMessageState = ::resetMessageState,
            onActivateAssets = ::activateAssets,
            onInactivateAssets = ::inactivateAssets,
            onUpdateAsset = ::onUpdateAsset
        )
    }

    private fun initAssets(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1000)

            if (isRefresh) {
                _uiState.value = _uiState.value.copy(
                    searchQuery = "",
                    filterData = HomeFilterData(),
                )
            }

            val assets = DataDummy.getAssets()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                assetDefault = assets,
                assets = assets
            )
        }
    }

    private fun search(query: String) {
        val query = query.trim()
        _uiState.value = _uiState.value.copy(searchQuery = query)

        val itemsFilter = _uiState.value.assetDefault
        val items = if (query.isBlank()) {
            itemsFilter
        } else {
            itemsFilter.filter { item ->
                val queryMatch = listOf(
                    item.name,
                    item.city,
                    item.picName
                ).any {
                    it.contains(query, ignoreCase = true)
                }

                val itemQueryMatch = item.orderList.any { orderItem ->
                    orderItem.item.name.contains(query, ignoreCase = true)
                }

                queryMatch || itemQueryMatch
            }
        }
        updateAssets(items)
    }

    private fun updateAssets(assets: List<Asset>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1000)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                assets = assets
            )
        }
    }

    private fun updateItemSelected(asset: Asset) {
        val selectedItems = _uiState.value.itemSelected.toMutableList()
        _uiState.value = _uiState.value.copy(
            itemSelected = if (selectedItems.contains(asset)) {
                selectedItems.apply { remove(asset) }
            } else {
                selectedItems.apply { add(asset) }
            }
        )
    }

    private fun toggleSelectAll() {
        _uiState.update {
            it.copy(
                itemSelected = if (it.isAllSelected) {
                    emptyList()
                } else {
                    it.assets
                },
                isAllSelected = !it.isAllSelected
            )
        }
    }

    private fun updateFilter(data: HomeFilterData) {
        _uiState.value = _uiState.value.copy(filterData = data)

        val itemsFilter = _uiState.value.assetDefault
        val items = itemsFilter.filter { item ->
            (data.activeSelected.isEmpty() || data.activeSelected.contains(item.active)) &&
                    (data.supplierSelected.isEmpty() || data.supplierSelected.contains(item.name)) &&
                    (data.citySelected.isEmpty() || data.citySelected.contains(item.city)) &&
                    (data.picSelected.isEmpty() || data.picSelected.contains(item.picName)) &&
                    (data.itemSelected.isEmpty() || item.orderList.any { orderItem ->
                        data.itemSelected.contains(orderItem.item.name)
                    }
                            )
        }
        updateAssets(items)
    }

    private fun deleteAssets(items: List<Asset>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingOverlay = true)
            delay(500)

            val isRand = (0..1).random() == 1
            if (isRand) {
                _uiState.update { currData ->
                    updateAssets(currData.assets.filterNot { items.contains(it) })
                    currData.copy(
                        isLoadingOverlay = false,
                        itemSelected = emptyList(),
                        deleteState = true
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoadingOverlay = false,
                    deleteState = false
                )
            }
        }
    }

    private fun resetMessageState() {
        _uiState.value = _uiState.value.copy(deleteState = null)
    }

    private fun onUpdateAsset(data: Asset) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val assets = _uiState.value.assets.toMutableList()

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
                assets = assets,
                assetDefault = assets
            )
        }
    }

    private fun updateAssetsStatus(items: List<Asset>, newStatus: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingOverlay = true)

            val itemsToUpdate = items.map { it.id }.toSet()

            val updatedAssets = _uiState.value.assets.map { asset ->
                if (itemsToUpdate.contains(asset.id)) {
                    asset.copy(active = newStatus)
                } else {
                    asset
                }
            }

            _uiState.update {
                it.copy(
                    isLoadingOverlay = false,
                    assets = updatedAssets,
                    assetDefault = updatedAssets,
                    itemSelected = emptyList()
                )
            }
        }
    }

    private fun activateAssets(items: List<Asset>) {
        updateAssetsStatus(items = items, newStatus = true)
    }

    private fun inactivateAssets(items: List<Asset>) {
        updateAssetsStatus(items = items, newStatus = false)
    }
}