package com.example.mobilecodingstyleguideline.model.home

import com.example.apiservices.data.model.SupplierEntity

data class HomeCallback(
    val onRefresh: () -> Unit = {},
    val onFilter: (HomeFilterData) -> Unit = {},
    val onSearch: (String) -> Unit = {},
    val onShowSearch: (Boolean) -> Unit = {},
    val onUpdateItemSelected: (SupplierEntity) -> Unit = {},
    val onToggleSelectAll: () -> Unit = {},
    val onDeleteSuppliers: (List<String>) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
    val onUpdateSupplier: (SupplierEntity) -> Unit = {},
    val onEditStatusSupplier: (List<SupplierEntity>, Boolean) -> Unit = { _, _ -> },
    val onDownloadAssets: (List<SupplierEntity>) -> Unit = {}
)