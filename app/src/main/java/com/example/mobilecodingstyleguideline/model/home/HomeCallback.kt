package com.example.mobilecodingstyleguideline.model.home

import com.example.apiservices.data.model.SupplierEntity

data class HomeCallback(
    val onRefresh: () -> Unit = {},
    val onFilter: (HomeFilterData) -> Unit = {},
    val onSearch: (String) -> Unit = {},
    val onUpdateItemSelected: (SupplierEntity) -> Unit = {},
    val onToggleSelectAll: () -> Unit = {},
    val onDeleteSuppliers: (List<String>) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
    val onUpdateSupplier: (SupplierEntity) -> Unit = {},
    val onActivateSuppliers: (List<SupplierEntity>) -> Unit = {},
    val onInactivateSuppliers: (List<SupplierEntity>) -> Unit = {},
    val onDownloadAssets: (List<SupplierEntity>) -> Unit = {}
)