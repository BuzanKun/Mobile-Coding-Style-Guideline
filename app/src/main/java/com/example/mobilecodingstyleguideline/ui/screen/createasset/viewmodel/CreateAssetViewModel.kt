package com.example.mobilecodingstyleguideline.ui.screen.createasset.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetCallback
import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetFormData
import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetFormError
import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetFormOption
import com.example.mobilecodingstyleguideline.ui.screen.createasset.uistate.CreateAssetUiState
import com.example.mobilecodingstyleguideline.util.Asset
import com.example.mobilecodingstyleguideline.util.DataDummy
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreateAssetViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(CreateAssetUiState())
    val uiState = _uiState.asStateFlow()

    fun init(data: Asset? = null) {
        _uiState.value = _uiState.value.copy(
            assetId = "",
            formData = CreateAssetFormData(),
            formError = CreateAssetFormError(),
            submitState = null,
            isEditForm = false
        )

        data?.let { item ->
            _uiState.value = CreateAssetUiState(
                assetId = item.id,
                formData = CreateAssetFormData(
                    name = item.name,
                    orderList = item.orderList,
                    country = item.country,
                    state = item.state,
                    city = item.city,
                    zipCode = item.zipCode,
                    address = item.address,
                    countryCode = item.countryCode,
                    phoneNumber = item.phoneNumber,
                    picName = item.picName,
                    picCountryCode = item.picCountryCode,
                    picPhoneNumber = item.picPhoneNumber,
                    picEmail = item.picEmail
                ),
                isEditForm = true
            )
        }
        getFormOption()
    }

    fun getCallback(): CreateAssetCallback {
        return CreateAssetCallback(
            onClearField = ::clearField,
            onResetMessageState = ::resetMessageState,
            onUpdateFormData = ::updateFormData,
            onSubmitForm = ::submitForm,
            onUpdateStayOnForm = ::updateStayOnForm
        )
    }

    private fun getFormOption() {
        _uiState.value = uiState.value.copy(
            formOption = CreateAssetFormOption(
                itemMasterList = DataDummy.getItemMasterList(),
                itemNameList = DataDummy.generateOptionsDataString(DataDummy.getItemName()),
                country = DataDummy.generateOptionsDataString(DataDummy.getCountry()),
                state = DataDummy.generateOptionsDataString(DataDummy.getState()),
                city = DataDummy.generateOptionsDataString(DataDummy.getCity()),
            )
        )
    }

    private fun clearField() {
        _uiState.value = _uiState.value.copy(
            formData = CreateAssetFormData(),
            formError = CreateAssetFormError(),
            submitState = null
        )
    }

    private fun resetMessageState() {
        _uiState.value = _uiState.value.copy(submitState = null)
    }

    private fun updateFormData(formData: CreateAssetFormData) {
        _uiState.value = _uiState.value.copy(
            formData = formData,
            formError = CreateAssetFormError()
        )
    }

    private fun submitForm() {
        val data = _uiState.value.formData

        if (!formValidation(data)) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingOverlay = true)
            delay(1000)

            _uiState.value = _uiState.value.copy(
                isLoadingOverlay = false,
                submitState = (0..1).random() == 1
            )
        }
    }

    private fun formValidation(data: CreateAssetFormData): Boolean {
        var formError = CreateAssetFormError()

        if (data.name.isEmpty()) {
            formError = formError.copy(
                name = "Company name must not be empty"
            )
        } else if (data.name.length > 30) {
            formError = formError.copy(
                name = "Max. 30 characters"
            )
        }

        if (data.zipCode.length > 15) {
            formError = formError.copy(
                zipCode = "Max. 15 characters"
            )
        }
        if (data.address.length > 120) {
            formError = formError.copy(
                address = "Max. 120 characters"
            )
        }
        val nonNumericRegex = Regex("[^0-9]")

        if (data.phoneNumber.length > 15) {
            formError = formError.copy(
                phoneNumber = "Max. 15 characters"
            )
        } else if (nonNumericRegex.containsMatchIn(data.phoneNumber)) {
            formError = formError.copy(
                phoneNumber = "Phone number format is incorrect"
            )
        }

        if (data.picName.length > 30) {
            formError = formError.copy(
                picName = "Max. 30 characters"
            )
        }

        if (data.picPhoneNumber.length > 15) {
            formError = formError.copy(
                picPhoneNumber = "Max. 15 characters"
            )
        } else if (nonNumericRegex.containsMatchIn(data.picPhoneNumber)) {
            formError = formError.copy(
                picPhoneNumber = "Phone number format is incorrect"
            )
        }
        if (data.picEmail.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(data.picEmail)
                .matches()
        ) {
            formError = formError.copy(
                picEmail = "Email format is incorrect"
            )
        }

        _uiState.value = _uiState.value.copy(formError = formError)

        return !formError.hasError()
    }

    private fun updateStayOnForm() {
        _uiState.update { currData ->
            currData.copy(isStayOnForm = !currData.isStayOnForm)
        }
    }
}