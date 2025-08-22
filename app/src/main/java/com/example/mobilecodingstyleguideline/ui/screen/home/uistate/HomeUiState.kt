package com.example.mobilecodingstyleguideline.ui.screen.home.uistate

import com.example.mobilecodingstyleguideline.model.home.HomeFilterData
import com.example.mobilecodingstyleguideline.model.home.HomeFilterOption
import com.example.mobilecodingstyleguideline.util.Asset

data class HomeUiState(
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val isLoadingGroup: Boolean = false,
    val isAllSelected: Boolean = false,
    val itemSelected: List<Asset> = emptyList(),
    val searchQuery: String = "",
    val filterOption: HomeFilterOption = HomeFilterOption(),
    val filterData: HomeFilterData = HomeFilterData(),
    val assetDefault: List<Asset> = emptyList(),
    val assets: List<Asset> = emptyList(),
    val deleteState: Boolean? = null,
    val activateState: Boolean? = null,
    val inactivateState: Boolean? = null
)
