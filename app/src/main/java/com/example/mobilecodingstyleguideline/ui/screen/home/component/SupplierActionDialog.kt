package com.example.mobilecodingstyleguideline.ui.screen.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.apiservices.data.model.SupplierEntity
import com.tagsamurai.common.model.Severity
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.alertdialog.AlertDialog
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Utils.generateAnnotated

@Composable
fun SupplierActionDialog(
    onDismissRequest: (Boolean) -> Unit,
    supplies: List<SupplierEntity>,
    showDialog: Boolean,
    onDialogConfirm: (List<SupplierEntity>) -> Unit,
    status: Status,
) {
    if (showDialog && supplies.isNotEmpty()) {
        val isSingle = supplies.size == 1

        val dialogState = when (status) {
            Status.DELETE -> DialogState(
                titleText = "Delete Supplier",
                submitText = "Delete",
                severity = Severity.DANGER,
                icon = R.drawable.ic_delete_bin_6_line_24dp,
                iconColor = theme.danger,
                singleItemMessage = "|${supplies[0].companyName}| will be deleted. Are you sure you want to delete it?",
                multiItemMessage = "You have selected |${supplies.size} data| to be deleted. Are you sure you want to delete it?"

            )

            Status.DOWNLOAD -> DialogState(
                titleText = "Download",
                submitText = "Download",
                severity = Severity.INFO,
                icon = R.drawable.ic_file_download_line_24dp,
                iconColor = theme.primary,
                singleItemMessage = "File ${supplies[0].companyName}.xlsx will be downloaded. Are you sure you want to download it?",
                multiItemMessage = " File Supplier-List.xlsx will be downloaded. Are you sure you want to download it?"
            )

            Status.ACTIVE -> DialogState(
                titleText = "Activate Supplier",
                submitText = "Activate",
                severity = Severity.INFO,
                icon = R.drawable.ic_checkbox_circle_line_24dp,
                iconColor = theme.primary,
                singleItemMessage = "|${supplies[0].companyName}| will be activated. Are you sure you want to activate it?",
                multiItemMessage = "You have selected |${supplies.size} supplier(s)| to be activated. Are you sure you want to activate it?"
            )

            Status.INACTIVE -> DialogState(
                titleText = "Inactivate Supplier",
                submitText = "Inactivate",
                severity = Severity.DANGER,
                icon = R.drawable.ic_information_line_24dp,
                iconColor = theme.danger,
                singleItemMessage = "|${supplies[0].companyName}| will be inactivated. Are you sure you want to inactivate it?",
                multiItemMessage = "You have selected |${supplies.size} supplier(s)| to be inactivated. Are you sure you want to inactivate it?"
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
                onDialogConfirm(supplies)
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
    val singleItemMessage: String,
    val multiItemMessage: String,
    val severity: Severity,
    val icon: Int,
    val iconColor: Color,
)

enum class Status { ACTIVE, INACTIVE, DELETE, DOWNLOAD }