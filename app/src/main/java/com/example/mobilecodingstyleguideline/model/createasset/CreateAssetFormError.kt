package com.example.mobilecodingstyleguideline.model.createasset

data class CreateAssetFormError(
    val name: String? = null,
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