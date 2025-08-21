package com.example.mobilecodingstyleguideline.ui.screen.createasset.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetFormData
import com.example.mobilecodingstyleguideline.ui.screen.createasset.uistate.CreateAssetUiState
import com.tagsamurai.common.model.TypeButton
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.button.Button
import com.tagsamurai.tscomponents.button.SingleSelector
import com.tagsamurai.tscomponents.textfield.PhoneNumberTextField
import com.tagsamurai.tscomponents.textfield.TextField

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
        Button(
            onClick = {
            },
            type = TypeButton.OUTLINED,
            text = "Supplied Item",
            leadingIcon = R.drawable.ic_add_fill_24dp,
            modifier = Modifier.fillMaxWidth()
        )

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