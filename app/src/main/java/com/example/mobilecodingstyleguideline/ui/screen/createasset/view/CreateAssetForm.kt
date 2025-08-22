package com.example.mobilecodingstyleguideline.ui.screen.createasset.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetFormData
import com.example.mobilecodingstyleguideline.ui.screen.createasset.uistate.CreateAssetUiState
import com.example.mobilecodingstyleguideline.util.DataDummy
import com.example.mobilecodingstyleguideline.util.Item
import com.example.mobilecodingstyleguideline.util.OrderItem
import com.tagsamurai.common.model.Severity
import com.tagsamurai.common.model.TypeButton
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.button.Button
import com.tagsamurai.tscomponents.button.MultiSelector
import com.tagsamurai.tscomponents.button.SingleSelector
import com.tagsamurai.tscomponents.textfield.PhoneNumberTextField
import com.tagsamurai.tscomponents.textfield.TextField
import com.tagsamurai.tscomponents.utils.Spacer.widthBox
import com.tagsamurai.tscomponents.utils.itemGap8

@Composable
fun CreateAssetForm(
    uiState: CreateAssetUiState,
    onUpdateForm: (CreateAssetFormData) -> Unit
) {
    var isReset by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.formData) {
        isReset = uiState.formData == CreateAssetFormData() && !isReset
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Company Name Text Field
        TextField(
            onValueChange = { result ->
                onUpdateForm(
                    uiState.formData.copy(name = result)
                )
            },
            placeholder = "Enter company name",
            value = uiState.formData.name,
            title = "Company Name",
            required = true,
            singleLine = true,
            isError = uiState.formError.name != null,
            textError = uiState.formError.name
        )
        // Add Item Button
        Button(
            onClick = {
                val currentList = uiState.formData.orderList
                val newList =
                    currentList + OrderItem(item = Item("", emptyList()), orderedSku = emptyList())
                onUpdateForm(
                    uiState.formData.copy(
                        orderList = newList
                    )
                )
            },
            type = TypeButton.OUTLINED,
            text = "Supplied Item",
            leadingIcon = R.drawable.ic_add_fill_24dp,
            modifier = Modifier.fillMaxWidth()
        )
        // Order Item List
        uiState.formData.orderList.forEachIndexed { index, orderItem ->
            OrderItemRow(
                uiState = uiState,
                orderItem = orderItem,
                itemIndex = index,
                onUpdateForm = onUpdateForm
            )
        }
        // Country Selector
        SingleSelector(
            onValueChange = { result ->
                onUpdateForm(
                    uiState.formData.copy(country = result)
                )
            },
            placeHolder = "Select country",
            items = uiState.formOption.country,
            value = uiState.formData.country,
            title = "Country",
            required = false,
        )
        // State Selector
        SingleSelector(
            onValueChange = { result ->
                onUpdateForm(
                    uiState.formData.copy(state = result)
                )
            },
            placeHolder = "Select state",
            items = uiState.formOption.state,
            value = uiState.formData.state,
            title = "State",
            required = false
        )
        // City Selector
        SingleSelector(
            onValueChange = { result ->
                onUpdateForm(
                    uiState.formData.copy(city = result)
                )
            },
            placeHolder = "Select city",
            items = uiState.formOption.city,
            value = uiState.formData.city,
            title = "City",
            required = false
        )
        // ZIP Code Text Field
        TextField(
            onValueChange = { result ->
                onUpdateForm(
                    uiState.formData.copy(zipCode = result)
                )
            },
            placeholder = "Enter ZIP Code",
            value = uiState.formData.zipCode,
            title = "ZIP Code",
            required = false,
            singleLine = true,
            isError = uiState.formError.zipCode != null,
            textError = uiState.formError.zipCode
        )
        // Company Address Text Field
        TextField(
            onValueChange = { result ->
                onUpdateForm(
                    uiState.formData.copy(address = result)
                )
            },
            placeholder = "Enter company address",
            value = uiState.formData.address,
            title = "Company Address",
            required = false,
            isError = uiState.formError.address != null,
            textError = uiState.formError.address
        )
        // Company Phone Number Text Field
        PhoneNumberTextField(
            onValueChange = { dial, result ->
                onUpdateForm(
                    uiState.formData.copy(
                        countryCode = dial,
                        phoneNumber = result
                    )
                )
            },
            placeholder = "Enter company phone number",
            dialCode = uiState.formData.countryCode,
            value = uiState.formData.phoneNumber,
            title = "Company Phone Number",
            isError = uiState.formError.phoneNumber != null,
            textError = uiState.formError.phoneNumber
        )
        // PIC Name Text Field
        TextField(
            onValueChange = { result ->
                onUpdateForm(
                    uiState.formData.copy(
                        picName = result
                    )
                )
            },
            placeholder = "Enter PIC Name",
            value = uiState.formData.picName,
            title = "PIC Name",
            required = false,
            isError = uiState.formError.picName != null,
            textError = uiState.formError.picName
        )
        // PIC Phone Number Text Field
        PhoneNumberTextField(
            onValueChange = { dial, result ->
                onUpdateForm(
                    uiState.formData.copy(
                        picCountryCode = dial,
                        picPhoneNumber = result
                    )
                )
            },
            placeholder = "Enter PIC contact number",
            dialCode = uiState.formData.picCountryCode,
            value = uiState.formData.picPhoneNumber,
            title = "Company Phone Number",
            isError = uiState.formError.picPhoneNumber != null,
            textError = uiState.formError.picPhoneNumber
        )
        // PIC Email Text Field
        TextField(
            onValueChange = { result ->
                onUpdateForm(
                    uiState.formData.copy(
                        picEmail = result
                    )
                )
            },
            placeholder = "Enter PIC email",
            value = uiState.formData.picEmail,
            title = "PIC Email",
            required = false,
            isError = uiState.formError.picEmail != null,
            textError = uiState.formError.picEmail
        )
    }
}

@Composable
fun OrderItemRow(
    uiState: CreateAssetUiState,
    orderItem: OrderItem,
    itemIndex: Int,
    onUpdateForm: (CreateAssetFormData) -> Unit
) {
    val isMultiItem = uiState.formData.orderList.size > 1

    val selectedItemNames = uiState.formData.orderList.map { it.item.name }

    val availableItemOptions = uiState.formOption.itemNameList.filter { itemName ->
        itemName.value !in selectedItemNames || itemName.value == orderItem.item.name
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {
        // Item Selector
        SingleSelector(
            onValueChange = { result ->
                val newItem = uiState.formOption.itemMasterList.find { it.name == result }
                if (newItem != null) {
                    val updateOrderItem = orderItem.copy(item = newItem, orderedSku = emptyList())
                    val newList = uiState.formData.orderList.toMutableList()
                    newList[itemIndex] = updateOrderItem
                    onUpdateForm(
                        uiState.formData.copy(
                            orderList = newList
                        )
                    )
                }
            },
            placeHolder = "Select item name",
            items = availableItemOptions,
            value = orderItem.item.name,
            title = "Item Name",
            required = true,
            modifier = Modifier.fillMaxWidth(if (isMultiItem) 0.4f else 0.5f)
        )
        itemGap8.widthBox()
        // SKU Selector
        MultiSelector(
            onValueChange = { result ->
                val newSku = orderItem.item.avalaibleSKUs.filter { sku ->
                    result.contains(sku.id)
                }
                val updatedOrderItem = orderItem.copy(orderedSku = newSku)
                val newList = uiState.formData.orderList.toMutableList()
                newList[itemIndex] = updatedOrderItem
                onUpdateForm(
                    uiState.formData.copy(
                        orderList = newList
                    )
                )
            },
            placeHolder = "Select SKU",
            items = DataDummy.generateOptionsDataString(orderItem.item.avalaibleSKUs.map { it.id }),
            value = orderItem.orderedSku.map { it.id },
            title = "SKU",
            required = true,
            isUseChip = true,
            enabled = orderItem.item.name.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(if (isMultiItem) 0.76f else 1f)
        )
        itemGap8.widthBox()
        if (uiState.formData.orderList.size > 1) {
            Button(
                onClick = {
                    val currentList = uiState.formData.orderList
                    val newList = currentList - orderItem
                    onUpdateForm(
                        uiState.formData.copy(
                            orderList = newList
                        )
                    )
                },
                type = TypeButton.OUTLINED,
                severity = Severity.DANGER,
                leadingIcon = R.drawable.ic_subtract_line_24dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 24.dp)
            )
        }
    }
}