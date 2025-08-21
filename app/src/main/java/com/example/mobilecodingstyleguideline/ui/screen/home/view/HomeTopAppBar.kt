package com.example.mobilecodingstyleguideline.ui.screen.home.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.ui.screen.home.component.AssetActionDialog
import com.example.mobilecodingstyleguideline.ui.screen.home.component.Status
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.tagsamurai.tscomponents.menu.model.Menu
import com.tagsamurai.tscomponents.textfield.SearchFieldTopAppBar
import com.tagsamurai.tscomponents.topappbar.TopAppBar

@Composable
fun HomeTopAppBar(
    uiState: HomeUiState,
    homeCallback: HomeCallback
) {
    var showSearch by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showActionSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showActivateDialog by remember { mutableStateOf(false) }
    var showInactivateDialog by remember { mutableStateOf(false) }
    val listMenu = getListMenu(uiState)

    if (showSearch) {
        SearchFieldTopAppBar(
            onNavigateUp = { showSearch = false },
            onSearchConfirm = homeCallback.onSearch
        )
    } else {
        TopAppBar(
            menu = listMenu,
            canNavigateBack = true,
            onMenuAction = { menu ->
                when (menu) {
                    Menu.SEARCH -> showSearch = true
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
        onActivate = { showActivateDialog = true },
        onInactivate = { showInactivateDialog = true }
    )

    // Delete Dialog
    AssetActionDialog(
        onDismissRequest = { state -> showDeleteDialog = state },
        assets = uiState.itemSelected,
        showDialog = showDeleteDialog,
        onDialogConfirm = { value ->
            showActionSheet = false
            homeCallback.onDeleteAssets(value)
        },
        status = Status.DELETE
    )

    // Download Dialog
    AssetActionDialog(
        onDismissRequest = { state -> showDownloadDialog = state },
        assets = uiState.assets,
        showDialog = showDownloadDialog,
        onDialogConfirm = { value ->
            homeCallback.onDownloadAssets(value)
        },
        status = Status.DOWNLOAD
    )

    // Activate Dialog
    AssetActionDialog(
        onDismissRequest = { state -> showActivateDialog = state },
        assets = uiState.itemSelected,
        showDialog = showActivateDialog,
        onDialogConfirm = { value ->
            showActionSheet = false
            homeCallback.onActivateAssets(value)
        },
        status = Status.ACTIVE
    )

    // Inactivate Dialog
    AssetActionDialog(
        onDismissRequest = { state -> showInactivateDialog = state },
        assets = uiState.itemSelected,
        showDialog = showInactivateDialog,
        onDialogConfirm = { value ->
            showActionSheet = false
            homeCallback.onInactivateAssets(value)
        },
        status = Status.INACTIVE
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