package com.example.mobilecodingstyleguideline.ui.screen.home.view.listsection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apiservices.data.model.SupplierEntity
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.navigation.NavigationRoute
import com.example.mobilecodingstyleguideline.ui.screen.home.component.AssetActionDialog
import com.example.mobilecodingstyleguideline.ui.screen.home.component.Status
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.example.mobilecodingstyleguideline.ui.screen.home.view.HomeActionSheet
import com.example.mobilecodingstyleguideline.util.DateTime
import com.tagsamurai.common.model.Severity
import com.tagsamurai.common.model.TypeChip
import com.tagsamurai.tscomponents.card.AdaptiveCardItem
import com.tagsamurai.tscomponents.chip.Chip
import com.tagsamurai.tscomponents.textfield.UserRecord
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.Spacer.widthBox
import com.tagsamurai.tscomponents.utils.bodyStyle
import com.tagsamurai.tscomponents.utils.itemGap4

@Composable
fun HomeItem(
    uiState: HomeUiState,
    item: SupplierEntity,
    homeCallback: HomeCallback,
    onNavigateTo: (String) -> Unit,
    onEditAsset: (SupplierEntity) -> Unit
) {
    val isSelected = uiState.itemSelected.contains(item)
    var showActionSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showActivateDialog by remember { mutableStateOf(false) }
    var showInactivateDialog by remember { mutableStateOf(false) }
    val chipItemLimit = 2

    val location =
        when {
            item.city.isNotEmpty() && item.country.isNotEmpty() -> {
                "${item.city}, ${item.country}"
            }

            item.city.isNotEmpty() -> item.city
            item.country.isNotEmpty() -> item.country
            else -> "No Location"
        }
    AdaptiveCardItem(
        showMoreIcon = true,
        onClick = { if (uiState.itemSelected.isNotEmpty()) homeCallback.onUpdateItemSelected(item) },
        onLongClick = { homeCallback.onUpdateItemSelected(item) },
        containerColor = if (isSelected) theme.popupBackgroundSelected else Color.Transparent,
        onClickAction = { showActionSheet = true }
    ) {
        Column {
            ItemState(
                active = item.status
            )
            itemGap4.heightBox()
            Text(
                text = item.companyName,
                style = bodyStyle,
                fontWeight = FontWeight.SemiBold
            )
            itemGap4.heightBox()
            Text(
                text = location,
                style = bodyStyle
            )
            Spacer(Modifier.height(4.dp))

            if (item.item.isNotEmpty()) {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(item.item.take(chipItemLimit)) { order ->
                        Chip(
                            label = order.itemName,
                            type = TypeChip.PILL,
                            severity = Severity.DARK
                        )
                        4.widthBox()
                    }
                    if (item.item.size > chipItemLimit) {
                        val remainingChipItemCount = item.item.size - chipItemLimit
                        item {
                            Text(
                                text = "+$remainingChipItemCount more",
                                style = bodyStyle,
                                color = theme.fieldPlaceholder
                            )
                        }
                    }
                }
            } else {
                Chip(
                    label = "No Order",
                    type = TypeChip.PILL,
                    severity = Severity.DARK
                )
            }
            itemGap4.heightBox()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = DateTime.formatDateTime(item.updatedAt),
                    style = bodyStyle
                )
                Spacer(Modifier.weight(1f))
                UserRecord(
                    username = item.picName.ifEmpty { "Unknown" }
                )
            }
        }
    }

    HomeActionSheet(
        onDismissRequest = { showActionSheet = it },
        uiState = uiState,
        showSheet = showActionSheet,
        onDetail = { onNavigateTo(NavigationRoute.DetailScreen.route) },
        onEdit = {
            showActionSheet = false
            onEditAsset(item)
        },
        onDelete = { showDeleteDialog = true },
        onActivate = { showActivateDialog = true },
        onInactivate = { showInactivateDialog = true },
        item = item
    )

    // Delete Dialog
    AssetActionDialog(
        onDismissRequest = { showDeleteDialog = it },
        supplies = listOf(item),
        showDialog = showDeleteDialog,
        onDialogConfirm = { value ->
            showActionSheet = false
            homeCallback.onDeleteSuppliers(value.map { it.id })
        },
        status = Status.DELETE
    )

    // Activate Dialog
    AssetActionDialog(
        onDismissRequest = { state -> showActivateDialog = state },
        supplies = listOf(item),
        showDialog = showActivateDialog,
        onDialogConfirm = { value ->
            showActionSheet = false
            homeCallback.onActivateSuppliers(value)
        },
        status = Status.ACTIVE
    )

    // Inactivate Dialog
    AssetActionDialog(
        onDismissRequest = { state -> showInactivateDialog = state },
        supplies = listOf(item),
        showDialog = showInactivateDialog,
        onDialogConfirm = { value ->
            showActionSheet = false
            homeCallback.onInactivateSuppliers(value)
        },
        status = Status.INACTIVE
    )
}

@Composable
fun ItemState(active: Boolean) {
    val label = if (active) "Active" else "Inactive"
    val severityState = if (active) Severity.SUCCESS else Severity.DANGER
    Chip(
        label = label,
        severity = severityState,
        type = TypeChip.BULLET
    )
}

@Preview
@Composable
private fun HomeItemPreview() {
    HomeItem(
        item = SupplierEntity(
            id = "1",
            status = true,
            companyName = "PT. ABC Indonesia",
            country = "Indonesia",
            state = "DKI Jakarta",
            city = "Jakarta Utara",
            picName = "Nakamoto Y",
            updatedAt = DateTime.formatDateTime(DateTime.getCurrentDateTime())
        ),
        homeCallback = HomeCallback(),
        uiState = HomeUiState(),
        onNavigateTo = {},
        onEditAsset = {}
    )
}