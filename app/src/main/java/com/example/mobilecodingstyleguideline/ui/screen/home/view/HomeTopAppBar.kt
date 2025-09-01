package com.example.mobilecodingstyleguideline.ui.screen.home.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.ui.screen.home.component.Status
import com.example.mobilecodingstyleguideline.ui.screen.home.component.SupplierActionDialog
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.tagsamurai.tscomponents.menu.model.Menu
import com.tagsamurai.tscomponents.textfield.SearchFieldTopAppBar
import com.tagsamurai.tscomponents.topappbar.TopAppBar

@Composable
fun HomeTopAppBar(
    uiState: HomeUiState,
    homeCallback: HomeCallback
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    var showActionSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showStatusChangeDialog by remember { mutableStateOf(false) }
    var newStatusToApply by remember { mutableStateOf(true) }

    val listMenu = getListMenu(uiState)

    if (uiState.showSearch) {
        SearchFieldTopAppBar(
            onNavigateUp = { homeCallback.onShowSearch(false) },
            onSearchConfirm = homeCallback.onSearch
        )
    } else {
        TopAppBar(
            menu = listMenu,
            canNavigateBack = true,
            onMenuAction = { menu ->
                when (menu) {
                    Menu.SEARCH -> homeCallback.onShowSearch(true)
                    Menu.FILTER -> showFilterSheet = true
                    Menu.SELECT_ALL, Menu.UNSELECT_ALL -> homeCallback.onToggleSelectAll()
                    Menu.OTHER -> showActionSheet = true
                    Menu.DOWNLOAD -> showDownloadDialog = true
                    Menu.LOG -> {}

                    else -> Unit
                }
            },
            title = if (uiState.itemSelected.isNotEmpty()) "${uiState.itemSelected.size}" else "",
            navigateUp = {},
        )
    }

    HomeFilterSheet(
        onDismissRequest = { state -> showFilterSheet = state },
        uiState = uiState,
        showFilter = showFilterSheet,
        onApplyConfirm = homeCallback.onFilter
    )

    HomeActionSheet(
        onDismissRequest = { state -> showActionSheet = state },
        showSheet = showActionSheet,
        uiState = uiState,
        onDelete = { showDeleteDialog = true },
        onStatusChange = { newStatus ->
            showStatusChangeDialog = true
            newStatusToApply = newStatus
        }
    )

    // Delete Dialog
    SupplierActionDialog(
        onDismissRequest = { state -> showDeleteDialog = state },
        supplies = uiState.itemSelected,
        showDialog = showDeleteDialog,
        onDialogConfirm = { value ->
            showActionSheet = false
            homeCallback.onDeleteSuppliers(value.map { it.id })
        },
        status = Status.DELETE
    )

    // Download Dialog
    SupplierActionDialog(
        onDismissRequest = { state -> showDownloadDialog = state },
        supplies = uiState.supplier,
        showDialog = showDownloadDialog,
        onDialogConfirm = { value ->
            homeCallback.onDownloadAssets(value)
        },
        status = Status.DOWNLOAD
    )

    // Status Change Dialog Dialog
    SupplierActionDialog(
        onDismissRequest = { state -> showStatusChangeDialog = state },
        supplies = uiState.itemSelected,
        showDialog = showStatusChangeDialog,
        onDialogConfirm = { value ->
            showActionSheet = false
            homeCallback.onEditStatusSupplier(value, newStatusToApply)
        },
        status = if (newStatusToApply) Status.INACTIVE else Status.ACTIVE
    )
}

fun getListMenu(uiState: HomeUiState): List<Menu> {
    val selectingMenu = listOf(
        Menu.SEARCH,
        if (uiState.isAllSelected) {
            Menu.SELECT_ALL
        } else Menu.UNSELECT_ALL,
        Menu.OTHER
    )

    val notSelectingMenu = listOf(
        Menu.SEARCH,
        Menu.FILTER,
        Menu.DOWNLOAD,
        Menu.LOG
    )

    return if (uiState.itemSelected.isNotEmpty()) selectingMenu else notSelectingMenu
}