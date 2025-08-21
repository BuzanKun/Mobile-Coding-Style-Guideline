package com.example.mobilecodingstyleguideline.model.home

import com.example.mobilecodingstyleguideline.util.Asset

data class HomeCallback(
    val onRefresh: () -> Unit = {},
    val onFilter: (HomeFilterData) -> Unit = {},
    val onSearch: (String) -> Unit = {},
    val onUpdateItemSelected: (Asset) -> Unit = {},
    val onToggleSelectAll: () -> Unit = {},
    val onDeleteAssets: (List<Asset>) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
    val onUpdateAsset: (Asset) -> Unit = {},
    val onActivateAssets: (List<Asset>) -> Unit = {},
    val onInactivateAssets: (List<Asset>) -> Unit = {},
    val onDownloadAssets: (List<Asset>) -> Unit = {}
)