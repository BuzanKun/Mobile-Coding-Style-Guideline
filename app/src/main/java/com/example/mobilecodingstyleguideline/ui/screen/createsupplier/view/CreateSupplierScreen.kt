package com.example.mobilecodingstyleguideline.ui.screen.createsupplier.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.example.apiservices.data.model.SupplierEntity
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierCallback
import com.example.mobilecodingstyleguideline.ui.screen.createsupplier.uistate.CreateSupplierUiState
import com.tagsamurai.tscomponents.button.DoubleActionButton
import com.tagsamurai.tscomponents.checkbox.CustomCheckbox
import com.tagsamurai.tscomponents.handlestate.HandleState
import com.tagsamurai.tscomponents.scaffold.Scaffold
import com.tagsamurai.tscomponents.snackbar.OnShowSnackBar
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.topappbar.TopAppBar
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.bodyStyle

@Composable
fun CreateAssetScreen(
    uiState: CreateSupplierUiState,
    callback: CreateSupplierCallback,
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
    onSubmit: (SupplierEntity) -> Unit
) {
    val successMessage: String
    val errorMessage: String
    val submitText: String
    val titleText: String

    if (uiState.isEditForm) {
        successMessage = "Success, supplier has been edited"
        errorMessage = "Error failed to edit supplier. Please check your connection and try again"
        submitText = "Edit"
        titleText = "Edit Supplier"
    } else {
        successMessage = "Success, supplier has been added"
        errorMessage = "Error failed to add supplier. Please check your connection and try again"
        submitText = "Create"
        titleText = "Create Supplier"
    }

    HandleState(
        state = uiState.submitState,
        onShowSnackBar = onShowSnackBar,
        successMsg = successMessage,
        errorMsg = errorMessage,
        onSuccess = {
            if (!uiState.isStayOnForm) {
                onNavigateUp()
            }
            onSubmit(uiState.data)
        },
        onDispose = callback.onResetMessageState
    )

    Scaffold(
        topBar = {
            TopAppBar(
                menu = emptyList(),
                canNavigateBack = true,
                onMenuAction = {},
                navigateUp = onNavigateUp,
                title = titleText
            )
        },
        isShowLoadingOverlay = uiState.isLoadingOverlay
    ) {
        Column {
            Box(Modifier.weight(1f)) {
                CreateAssetForm(
                    uiState = uiState,
                    onUpdateForm = callback.onUpdateFormData
                )
            }
            DoubleActionButton(
                onCancelConfirm = callback.onClearField,
                onApplyConfirm = callback.onSubmitForm,
                labelCancel = "Clear field",
                labelApply = submitText,
                contentHeader = {
                    if (!uiState.isEditForm) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(
                                role = Role.Checkbox,
                                onClick = callback.onUpdateStayOnForm
                            )
                        ) {
                            Text(
                                text = "Stay on this form after submitting",
                                style = bodyStyle,
                                color = theme.bodyText,
                                modifier = Modifier.weight(1f)
                            )
                            CustomCheckbox(
                                checked = uiState.isStayOnForm,
                                onCheckedChange = { callback.onUpdateStayOnForm() }
                            )
                        }
                        10.heightBox()
                    }
                }
            )
        }
    }
}