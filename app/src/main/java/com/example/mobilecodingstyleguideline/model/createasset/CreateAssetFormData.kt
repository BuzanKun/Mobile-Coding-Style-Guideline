package com.example.mobilecodingstyleguideline.model.createasset

import com.example.mobilecodingstyleguideline.util.OrderItem

data class CreateAssetFormData(
    val name: String = "",
    val orderList: List<OrderItem> = emptyList(),
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