package com.example.mobilecodingstyleguideline.ui.screen.createasset.view

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetCallback
import com.example.mobilecodingstyleguideline.ui.screen.createasset.uistate.CreateAssetUiState
import com.example.mobilecodingstyleguideline.ui.screen.createasset.viewmodel.CreateAssetViewModel
import com.example.mobilecodingstyleguideline.util.Asset
import com.tagsamurai.tscomponents.snackbar.OnShowSnackBar
import com.tagsamurai.tscomponents.snackbar.Snackbar
import com.tagsamurai.tscomponents.snackbar.showSnackbar

@Composable
fun CreateAssetDialog(
    onDismissRequest: (Boolean) -> Unit,
    showDialog: Boolean,
    data: Asset? = null,
    onShowSnackBar: OnShowSnackBar,
    onSubmit: (Asset) -> Unit
) {
    val viewModel: CreateAssetViewModel = hiltViewModel()
    val uiState: CreateAssetUiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val callback = viewModel.getCallback()

    LaunchedEffect(showDialog) {
        if (showDialog) {
            viewModel.init(data = data)
        }
    }

    CreateAssetDialog(
        showDialog = showDialog,
        uiState = uiState,
        callback = callback,
        onNavigateUp = { onDismissRequest(false) },
        onShowSnackBar = onShowSnackBar,
        onSubmit = onSubmit
    )
}

@Composable
private fun CreateAssetDialog(
    showDialog: Boolean,
    uiState: CreateAssetUiState,
    callback: CreateAssetCallback,
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
    onSubmit: (Asset) -> Unit
) {
    val properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    fun showSnackbar(message: String, isSuccess: Boolean) {
        snackbarHostState.showSnackbar(scope = scope, message = message, isSuccess = isSuccess)
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = onNavigateUp,
            properties = properties
        ) {
            (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)
            Box {
                CreateAssetScreen(
                    uiState = uiState,
                    callback = callback,
                    onNavigateUp = onNavigateUp,
                    onShowSnackBar = { message, isSuccess ->
                        onShowSnackBar(message, isSuccess)
                        if (!isSuccess || uiState.isStayOnForm) {
                            showSnackbar(message, isSuccess)
                        }
                    },
                    onSubmit = onSubmit
                )
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}