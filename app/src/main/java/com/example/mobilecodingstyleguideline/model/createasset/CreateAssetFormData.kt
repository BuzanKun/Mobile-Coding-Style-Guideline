package com.example.mobilecodingstyleguideline.model.createasset

data class CreateAssetFormData(
    val name: String = "",
    val country: String = "",
    val state: String = "",
    val city: String = "",
    val zipCode: String = "",
    val address: String = "",
    val countryCode: String = "",
    val phoneNumber: String = "",
    val picName: String = "",
    val picCountryCode: String = "",
    val picPhoneNumber: String = "",
    val picEmail: String = ""
)