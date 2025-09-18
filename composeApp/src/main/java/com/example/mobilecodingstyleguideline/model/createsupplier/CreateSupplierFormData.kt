package com.example.mobilecodingstyleguideline.model.createsupplier

data class CreateSupplierFormData(
    val companyName: String = "",
    val items: List<Item> = listOf(
        Item(
            itemName = "",
            itemSku = emptyList()
        )
    ),
    val country: String = "",
    val state: String = "",
    val city: String = "",
    val zipCode: String = "",
    val companyLocation: String = "",
    val countryCode: String = "",
    val companyPhoneNumber: String = "",
    val picName: String = "",
    val picCountryCode: String = "",
    val picPhoneNumber: String = "",
    val picEmail: String = ""
) {
    data class Item(
        val id: String = "",
        val supplierId: String = "",
        val itemName: String = "",
        val itemSku: List<String> = emptyList()
    )
}