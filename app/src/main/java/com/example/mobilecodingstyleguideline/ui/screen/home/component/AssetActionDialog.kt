package com.example.mobilecodingstyleguideline.ui.screen.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.mobilecodingstyleguideline.util.Asset
import com.tagsamurai.common.model.Severity
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.alertdialog.AlertDialog
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Utils.generateAnnotated

@Composable
fun AssetActionDialog(
    onDismissRequest: (Boolean) -> Unit,
    assets: List<Asset>,
    showDialog: Boolean,
    onDialogConfirm: (List<Asset>) -> Unit,
    status: Status,
) {
    if (showDialog) {
        val isSingle = assets.size == 1

        val dialogState = when (status) {
            Status.DELETE -> DialogState(
                titleText = "Delete Supplier",
                submitText = "Delete",
                successMessage = "Success, supplier has been deleted.",
                errorMessage = "Error, failed to delete supplier. Please check your connection and try again.",
                severity = Severity.DANGER,
                icon = R.drawable.ic_delete_bin_6_line_24dp,
                iconColor = theme.danger,
                singleItemMessage = "|${assets[0].name}| will be deleted. Are you sure you want to delete it?",
                multiItemMessage = "You have selected |${assets.size} data| to be deleted. Are you sure you want to delete it?"

            )

            Status.DOWNLOAD -> DialogState(
                titleText = "Download",
                submitText = "Download",
                successMessage = "Success, data has been downloaded.",
                errorMessage = "Error, failed to download data. Please check your connection and try again.",
                severity = Severity.INFO,
                icon = R.drawable.ic_file_download_line_24dp,
                iconColor = theme.primary,
                singleItemMessage = "File ${assets[0].name}.xlsx will be downloaded. Are you sure you want to download it?",
                multiItemMessage = " File Supplier-List.xlsx will be downloaded. Are you sure you want to download it?"
            )

            Status.ACTIVE -> DialogState(
                titleText = "Activate Supplier",
                submitText = "Activate",
                successMessage = "Success, supplier has been activated.",
                errorMessage = "Error, failed to activate supplier. Please check your connection and try again.",
                severity = Severity.INFO,
                icon = R.drawable.ic_checkbox_circle_line_24dp,
                iconColor = theme.primary,
                singleItemMessage = "|${assets[0].name}| will be activated. Are you sure you want to activate it?",
                multiItemMessage = "You have selected |${assets.size} supplier(s)| to be activated. Are you sure you want to activate it?"
            )

            Status.INACTIVE -> DialogState(
                titleText = "Inactivate Supplier",
                submitText = "Inactivate",
                successMessage = "Successs, supplier has been inactivated.",
                errorMessage = "Error, failed to inactivate supplier. Please check your connection and try again.",
                severity = Severity.DANGER,
                icon = R.drawable.ic_information_line_24dp,
                iconColor = theme.danger,
                singleItemMessage = "|${assets[0].name}| will be inactivated. Are you sure you want to inactivate it?",
                multiItemMessage = "You have selected |${assets.size} supplier(s)| to be inactivated. Are you sure you want to inactivate it?"
            )
        }

        val rawContent =
            if (isSingle) dialogState.singleItemMessage else dialogState.multiItemMessage
        val message = rawContent.generateAnnotated()

        AlertDialog(
            onDismissRequest = onDismissRequest,
            onButtonCancel = { onDismissRequest(false) },
            onButtonConfirm = {
                onDismissRequest(false)
                onDialogConfirm(assets)
            },
            title = dialogState.titleText,
            textButtonCancel = "Cancel",
            textButtonConfirm = dialogState.submitText,
            content = message,
            severity = dialogState.severity,
            icon = dialogState.icon,
            iconColor = dialogState.iconColor
        )
    }
}

data class DialogState(
    val titleText: String,
    val submitText: String,
    val successMessage: String,
    val errorMessage: String,
    val singleItemMessage: String,
    val multiItemMessage: String,
    val severity: Severity,
    val icon: Int,
    val iconColor: Color,
)

enum class Status { ACTIVE, INACTIVE, DELETE, DOWNLOAD }