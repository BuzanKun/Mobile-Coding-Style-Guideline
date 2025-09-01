package com.example.mobilecodingstyleguideline.ui.screen.home.uistate

import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.mobilecodingstyleguideline.model.home.HomeFilterData
import com.example.mobilecodingstyleguideline.model.home.HomeFilterOption
import com.tagsamurai.tscomponents.utils.Utils

data class HomeUiState(
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val isLoadingGroup: Boolean = false,
    val isAllSelected: Boolean = false,
    val itemSelected: List<SupplierEntity> = emptyList(),
    val searchQuery: String = "",
    val showSearch: Boolean = false,
    val filterOption: HomeFilterOption = HomeFilterOption(),
    val filterData: HomeFilterData = HomeFilterData(),
    val supplierDefault: List<SupplierEntity> = emptyList(),
    val supplier: List<SupplierEntity> = emptyList(),
    val deleteState: Boolean? = null,
    val statusState: Boolean? = null,
    val isActive: Boolean = false

) {
    val queryParams = GetSupplierQueryParams(
        search = searchQuery.ifBlank { null },
        supplier = Utils.toJsonIfNotEmpty(filterData.supplierSelected),
        city = Utils.toJsonIfNotEmpty(filterData.citySelected),
        itemName = Utils.toJsonIfNotEmpty(filterData.itemSelected),
        isActive = Utils.toJsonIfNotEmpty(filterData.activeSelected),
        modifiedBy = Utils.toJsonIfNotEmpty(filterData.modifierSelected),
        updatedAt = Utils.toJsonIfNotEmpty(filterData.dateSelected)
    )
}
