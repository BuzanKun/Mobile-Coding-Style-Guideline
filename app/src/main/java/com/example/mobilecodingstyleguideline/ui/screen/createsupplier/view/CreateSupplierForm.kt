package com.example.mobilecodingstyleguideline.ui.screen.createsupplier.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormData
import com.example.mobilecodingstyleguideline.ui.screen.createsupplier.uistate.CreateSupplierUiState
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
fun CreateSupplierForm(
    uiState: CreateSupplierUiState,
    onUpdateForm: (CreateSupplierFormData) -> Unit
) {
    var isReset by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.formData) {
        isReset = uiState.formData == CreateSupplierFormData() && !isReset
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
                    uiState.formData.copy(companyName = result)
                )
            },
            placeholder = "Enter company name",
            value = uiState.formData.companyName,
            title = "Company Name",
            required = true,
            singleLine = true,
            isError = uiState.formError.companyName != null,
            textError = uiState.formError.companyName
        )
        // Add Item Button
        Button(
            onClick = {
                val currentList = uiState.formData.items
                val newList =
                    currentList + CreateSupplierFormData.Item(itemName = "", itemSku = emptyList())
                onUpdateForm(
                    uiState.formData.copy(
                        items = newList
                    )
                )
            },
            type = TypeButton.OUTLINED,
            text = "Supplied Item",
            leadingIcon = R.drawable.ic_add_fill_24dp,
            modifier = Modifier.fillMaxWidth()
        )
        // Order Item List
        uiState.formData.items.forEachIndexed { index, item ->
            ItemRow(
                uiState = uiState,
                item = item,
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
                    uiState.formData.copy(companyLocation = result)
                )
            },
            placeholder = "Enter company address",
            value = uiState.formData.companyLocation,
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
                        companyPhoneNumber = result
                    )
                )
            },
            placeholder = "Enter company phone number",
            dialCode = uiState.formData.countryCode,
            value = uiState.formData.companyPhoneNumber,
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
fun ItemRow(
    uiState: CreateSupplierUiState,
    item: CreateSupplierFormData.Item,
    itemIndex: Int,
    onUpdateForm: (CreateSupplierFormData) -> Unit
) {
    val isMultiItem = uiState.formData.items.size > 1

    val selectedItemNames = uiState.formData.items.map { it.itemName }

    val availableItemOptions = uiState.formOption.itemNameList.filter { itemName ->
        itemName.value !in selectedItemNames || itemName.value == item.itemName
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Item Selector
        Box(
            modifier = Modifier.weight(1f)
        ) {
            SingleSelector(
                onValueChange = { result ->
                    val updatedItem = item.copy(itemName = result, itemSku = emptyList())
                    val newList = uiState.formData.items.toMutableList()
                    newList[itemIndex] = updatedItem
                    onUpdateForm(
                        uiState.formData.copy(
                            items = newList
                        )
                    )
                },
                placeHolder = "Select item name",
                items = availableItemOptions,
                value = item.itemName,
                title = "Item Name",
                required = true,
                isError = uiState.formError.itemName.getOrNull(itemIndex) != null,
                textError = uiState.formError.itemName.getOrNull(itemIndex),
            )
        }

        itemGap8.widthBox()
        // SKU Selector
        Box(
            modifier = Modifier.weight(1f)
        ) {
            MultiSelector(
                onValueChange = { result ->
                    val updatedItem = item.copy(itemSku = result)
                    val newList = uiState.formData.items.toMutableList()
                    newList[itemIndex] = updatedItem
                    onUpdateForm(
                        uiState.formData.copy(
                            items = newList
                        )
                    )
                },
                placeHolder = "Select SKU",
                items = uiState.formOption.itemSkuList,
                value = item.itemSku,
                title = "SKU",
                required = true,
                isUseChip = true,
                enabled = item.itemName.isNotEmpty(),
                isError = item.itemName.isNotEmpty() && uiState.formError.itemSku.getOrNull(
                    itemIndex
                ) != null,
                textError = uiState.formError.itemSku.getOrNull(itemIndex),
            )
        }

        itemGap8.widthBox()
        if (isMultiItem) {
            Box {
                OutlinedIconButton(
                    onClick = {
                        val currentList = uiState.formData.items
                        val newList = currentList - item
                        onUpdateForm(
                            uiState.formData.copy(
                                items = newList
                            )
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 24.dp),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_subtract_line_24dp),
                        tint = Color.Red,
                        contentDescription = null
                    )
                }
            }
        }
    }
}