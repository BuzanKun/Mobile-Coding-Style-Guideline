package com.example.mobilecodingstyleguideline.ui.screen.home.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilecodingstyleguideline.model.home.HomeFilterData
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.tagsamurai.tscomponents.bottomsheet.FilterBottomSheet
import com.tagsamurai.tscomponents.chip.ChipSelectorWithOptionData

@Composable
fun HomeFilterSheet(
    onDismissRequest: (Boolean) -> Unit,
    uiState: HomeUiState,
    showFilter: Boolean,
    onApplyConfirm: (HomeFilterData) -> Unit
) {
    var tempFilterData by remember { mutableStateOf(uiState.filterData) }

    LaunchedEffect(uiState.filterData) {
        if (uiState.filterData != tempFilterData) {
            tempFilterData = uiState.filterData
        }
    }

    FilterBottomSheet(
        onDismissRequest = {
            tempFilterData = uiState.filterData
            onDismissRequest(false)
        },
        onApplyConfirm = {
            onApplyConfirm(tempFilterData)
            onDismissRequest(false)
        },
        onResetConfirm = {
            tempFilterData = HomeFilterData()
        },
        isItemSelected = tempFilterData != HomeFilterData(),
        isShowSheet = showFilter
    ) { reset ->
        ChipSelectorWithOptionData(
            title = "Active",
            value = tempFilterData.activeSelected,
            isReset = reset,
            items = uiState.filterOption.activeOption,
            onChipsSelected = { result ->
                tempFilterData = tempFilterData.copy(activeSelected = result)
            }
        )
        ChipSelectorWithOptionData(
            title = "Supplier",
            value = tempFilterData.supplierSelected,
            isReset = reset,
            items = uiState.filterOption.supplierOption,
            onChipsSelected = { result ->
                tempFilterData = tempFilterData.copy(supplierSelected = result)
            }
        )
        ChipSelectorWithOptionData(
            title = "City",
            value = tempFilterData.citySelected,
            isReset = reset,
            items = uiState.filterOption.cityOption,
            onChipsSelected = { result ->
                tempFilterData = tempFilterData.copy(citySelected = result)
            }
        )
        ChipSelectorWithOptionData(
            title = "Item Name",
            value = tempFilterData.itemSelected,
            isReset = reset,
            items = uiState.filterOption.itemOption,
            onChipsSelected = { result ->
                tempFilterData = tempFilterData.copy(itemSelected = result)
            }
        )
        ChipSelectorWithOptionData(
            title = "Modified By",
            value = tempFilterData.picSelected,
            isReset = reset,
            items = uiState.filterOption.picOption,
            onChipsSelected = { result ->
                tempFilterData = tempFilterData.copy(picSelected = result)
            }
        )
    }
}

@Preview
@Composable
private fun HomeFilterSheetPreview() {
    HomeFilterSheet(
        onDismissRequest = {},
        uiState = HomeUiState(),
        showFilter = true,
        onApplyConfirm = {}
    )
}