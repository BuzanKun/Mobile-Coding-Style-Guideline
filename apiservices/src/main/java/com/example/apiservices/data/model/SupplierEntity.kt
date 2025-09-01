package com.example.apiservices.data.model


data class SupplierEntity(
    val id: String = "",
    val status: Boolean = false,
    val companyName: String = "",
    val item: List<Item> = emptyList(),
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
    val picEmail: String = "",
    val modifiedBy: String = "",
    val updatedAt: String = "",
    val createdAt: String = ""
) {
    data class Item(
        val id: String = "",
        val supplierId: String = "",
        val itemName: String = "",
        val itemSku: List<String> = emptyList()
    )
}