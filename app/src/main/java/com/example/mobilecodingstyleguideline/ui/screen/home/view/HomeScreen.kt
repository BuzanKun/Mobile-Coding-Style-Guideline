package com.example.mobilecodingstyleguideline.ui.screen.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.apiservices.data.model.SupplierEntity
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.ui.screen.createsupplier.view.CreateAssetDialog
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.example.mobilecodingstyleguideline.ui.screen.home.view.listsection.HomeListSection
import com.example.mobilecodingstyleguideline.ui.screen.home.viewmodel.HomeViewModel
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.button.CustomFloatingIconButton
import com.tagsamurai.tscomponents.handlestate.HandleState
import com.tagsamurai.tscomponents.pagetitle.PageTitle
import com.tagsamurai.tscomponents.scaffold.Scaffold
import com.tagsamurai.tscomponents.snackbar.OnShowSnackBar
import com.tagsamurai.tscomponents.tab.TabList
import com.tagsamurai.tscomponents.theme.theme

@Composable
fun HomeScreen(onNavigateTo: (String) -> Unit, onShowSnackBar: OnShowSnackBar) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val uiState = homeViewModel.uiState.collectAsStateWithLifecycle()
    val homeCallback = homeViewModel.getCallback()

    LaunchedEffect(Unit) {
        homeViewModel.init()
    }

    HomeScreen(
        uiState = uiState.value,
        homeCallback = homeCallback,
        onShowSnackBar = onShowSnackBar,
        onNavigateTo = onNavigateTo
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    homeCallback: HomeCallback,
    onNavigateTo: (String) -> Unit,
    onShowSnackBar: OnShowSnackBar
) {
    var data: SupplierEntity? by remember { mutableStateOf(null) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val statusSuccessMessage: String
    val statusErrorMessage: String

    if (uiState.isActive) {
        statusSuccessMessage = "Success, supplier has been activated."
        statusErrorMessage =
            "Error, failed to activate supplier. Please check your connection and try again."
    } else {
        statusSuccessMessage = "Successs, supplier has been inactivated."
        statusErrorMessage =
            "Error, failed to inactivate supplier. Please check your connection and try again."
    }

    // Delete State
    HandleState(
        state = uiState.deleteState,
        onShowSnackBar = onShowSnackBar,
        successMsg = "Success, supplier has been deleted.",
        errorMsg = "Error, failed to delete supplier. Please check your connection and try again",
        onDispose = homeCallback.onResetMessageState
    )

    // Activate State
    HandleState(
        state = uiState.statusState,
        onShowSnackBar = onShowSnackBar,
        successMsg = statusSuccessMessage,
        errorMsg = statusErrorMessage,
        onDispose = homeCallback.onResetMessageState
    )

    Scaffold(
        topBar = {
            HomeTopAppBar(
                uiState = uiState,
                homeCallback = homeCallback
            )
        },
        floatingActionButton = {
            CustomFloatingIconButton(
                icon = R.drawable.ic_add_fill_24dp,
                containerColor = theme.warning500,
                iconColor = theme.warning500
            ) {
                showCreateDialog = true
            }
        },
        isShowLoadingOverlay = uiState.isLoadingOverlay
    ) {
        Column {
            PageTitle(
                title = "Supplier",
                bottomContent = {
                    TabList(
                        onTabChange = {},
                        tabs = listOf("List", "Supplier Activities"),
                        selectedTabIndex = 0,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                }
            )
            HomeListSection(
                uiState = uiState,
                homeCallback = homeCallback,
                onEditAsset = { value ->
                    data = value
                    showCreateDialog = true
                },
                onNavigateTo = onNavigateTo
            )
        }

        CreateAssetDialog(
            onDismissRequest = { state ->
                showCreateDialog = state
                data = null
            },
            showDialog = showCreateDialog,
            supplyId = data?.id,
            onShowSnackBar = onShowSnackBar,
            onSubmit = homeCallback.onUpdateSupplier
        )
    }
}