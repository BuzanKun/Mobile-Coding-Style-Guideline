package com.example.mobilecodingstyleguideline.ui.screen.home.uistate

import com.example.apiservices.data.model.SupplierEntity
import com.example.mobilecodingstyleguideline.model.home.HomeFilterData
import com.example.mobilecodingstyleguideline.model.home.HomeFilterOption

data class HomeUiState(
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val isLoadingGroup: Boolean = false,
    val isAllSelected: Boolean = false,
    val itemSelected: List<SupplierEntity> = emptyList(),
    val searchQuery: String = "",
    val filterOption: HomeFilterOption = HomeFilterOption(),
    val filterData: HomeFilterData = HomeFilterData(),
    val supplyDefault: List<SupplierEntity> = emptyList(),
    val supplier: List<SupplierEntity> = emptyList(),
    val deleteState: Boolean? = null,
    val activateState: Boolean? = null,
    val inactivateState: Boolean? = null
)
