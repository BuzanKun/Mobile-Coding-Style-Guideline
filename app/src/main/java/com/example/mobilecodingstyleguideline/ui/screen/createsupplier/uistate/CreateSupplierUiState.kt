package com.example.mobilecodingstyleguideline.ui.screen.createsupplier.uistate

import com.example.apiservices.data.model.SupplierEntity
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormData
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormError
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormOption
import com.example.mobilecodingstyleguideline.util.DateTime

data class CreateSupplierUiState(
    val isLoadingOverlay: Boolean = false,
    val isLoadingFormOption: Boolean = false,
    val isEditForm: Boolean = false,
    val isStayOnForm: Boolean = false,
    val assetId: String = "",
    val formData: CreateSupplierFormData = CreateSupplierFormData(),
    val formError: CreateSupplierFormError = CreateSupplierFormError(),
    val formOption: CreateSupplierFormOption = CreateSupplierFormOption(),
    val submitState: Boolean? = null
) {
    val filteredItems =
        formData.items.filter { it.itemName.isNotEmpty() && it.itemSku.isNotEmpty() }

    val data = SupplierEntity(
        id = assetId,
        status = true,
        item = filteredItems.map {
            SupplierEntity.Item(
                id = it.id,
                supplierId = it.supplierId,
                itemName = it.itemName,
                itemSku = it.itemSku
            )
        },
        companyName = formData.companyName,
        country = formData.country,
        state = formData.state,
        city = formData.city,
        zipCode = formData.zipCode,
        companyLocation = formData.companyLocation,
        countryCode = formData.countryCode,
        companyPhoneNumber = formData.companyPhoneNumber,
        picCountryCode = formData.picCountryCode,
        picName = formData.picName.ifEmpty { "Unknown" },
        picPhoneNumber = formData.picPhoneNumber,
        picEmail = formData.picEmail,
        updatedAt = DateTime.getCurrentDateTime()
    )
}