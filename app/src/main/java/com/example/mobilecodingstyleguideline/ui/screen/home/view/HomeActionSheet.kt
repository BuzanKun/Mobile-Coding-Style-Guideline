package com.example.mobilecodingstyleguideline.ui.screen.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.example.mobilecodingstyleguideline.util.Asset
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.bottomsheet.BottomSheet
import com.tagsamurai.tscomponents.button.ActionButton
import com.tagsamurai.tscomponents.switch.Switch
import com.tagsamurai.tscomponents.theme.theme
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.headerStyle
import com.tagsamurai.tscomponents.utils.itemGap8

@Composable
fun HomeActionSheet(
    onDismissRequest: (Boolean) -> Unit,
    showSheet: Boolean,
    uiState: HomeUiState,
    item: Asset? = null,
    onDetail: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
    onDelete: () -> Unit,
    onActivate: (() -> Unit)? = null,
    onInactivate: (() -> Unit)? = null
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        isShowSheet = showSheet,
    ) {
        Column {
            if (uiState.itemSelected.isEmpty() && item != null) {
                Row(
                    Modifier.padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = item.name,
                        style = headerStyle,
                        fontWeight = FontWeight.SemiBold,
                        color = theme.popupTitle
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = item.active,
                        onCheckedChange = {
                            if (item.active) onInactivate?.invoke() else onActivate?.invoke()
                        }
                    )
                }
                itemGap8.heightBox()
                ActionButton(
                    onClickAction = { onDetail?.invoke() },
                    icon = R.drawable.ic_file_info_line_24dp,
                    title = "Detail"
                )
                ActionButton(
                    onClickAction = { onEdit?.invoke() },
                    icon = R.drawable.ic_edit_2_line_24dp,
                    title = "Edit"
                )
            } else {
                ActionButton(
                    onClickAction = { onActivate?.invoke() },
                    icon = R.drawable.ic_check_line_24dp,
                    title = "Activate"
                )
                ActionButton(
                    onClickAction = { onInactivate?.invoke() },
                    icon = R.drawable.ic_close_line_24dp,
                    title = "Deactivate"
                )
            }
            ActionButton(
                onClickAction = onDelete,
                icon = R.drawable.ic_delete_bin_6_line_24dp,
                title = "Delete",
                iconTint = theme.danger,
                textColor = theme.danger
            )
        }
    }
}