package com.example.mobilecodingstyleguideline.model.createsupplier

data class CreateSupplierFormError(
    val name: String? = null,
    val itemName: List<String?> = emptyList(),
    val itemSku: List<String?> = emptyList(),
    val zipCode: String? = null,
    val address: String? = null,
    val countryCode: String? = null,
    val phoneNumber: String? = null,
    val picName: String? = null,
    val picCountryCode: String? = null,
    val picPhoneNumber: String? = null,
    val picEmail: String? = null
) {
    fun hasError(): Boolean {
        return name != null ||
                itemName.any { it != null } ||
                itemSku.any { it != null } ||
                zipCode != null ||
                address != null ||
                countryCode != null ||
                phoneNumber != null ||
                picName != null ||
                picCountryCode != null ||
                picPhoneNumber != null ||
                picEmail != null
    }
}