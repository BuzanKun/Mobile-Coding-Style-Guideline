package com.example.mobilecodingstyleguideline.ui.screen.createasset.uistate

import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetFormData
import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetFormError
import com.example.mobilecodingstyleguideline.model.createasset.CreateAssetFormOption
import com.example.mobilecodingstyleguideline.util.Asset
import com.example.mobilecodingstyleguideline.util.DateTime

data class CreateAssetUiState(
    val isLoadingOverlay: Boolean = false,
    val isEditForm: Boolean = false,
    val isStayOnForm: Boolean = false,
    val assetId: String = "",
    val formData: CreateAssetFormData = CreateAssetFormData(),
    val formError: CreateAssetFormError = CreateAssetFormError(),
    val formOption: CreateAssetFormOption = CreateAssetFormOption(),
    val submitState: Boolean? = null
) {
    val data = Asset(
        id = assetId,
        active = true,
        name = formData.name,
        country = formData.country,
        state = formData.state,
        city = formData.city,
        zipCode = formData.zipCode,
        address = formData.address,
        countryCode = formData.countryCode,
        phoneNumber = formData.phoneNumber,
        picCountryCode = formData.picCountryCode,
        picName = formData.picName.ifEmpty { "Unknown" },
        picPhoneNumber = formData.picPhoneNumber,
        picEmail = formData.picEmail,
        lastModified = DateTime.getCurrentDateTime()
    )
}