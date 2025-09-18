package com.example.shared.data.source.network.model.request.supplier

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUpdateSupplierBody(
    @SerialName("companyName")
    val companyName: String = "",
    @SerialName("item")
    val item: List<Item> = emptyList(),
    @SerialName("country")
    val country: String = "",
    @SerialName("state")
    val state: String = "",
    @SerialName("city")
    val city: String = "",
    @SerialName("zipCode")
    val zipCode: String = "",
    @SerialName("companyLocation")
    val companyLocation: String = "",
    @SerialName("companyPhoneNumber")
    val companyPhoneNumber: String = "",
    @SerialName("picName")
    val picName: String = "",
    @SerialName("picPhoneNumber")
    val picPhoneNumber: String = "",
    @SerialName("picEmail")
    val picEmail: String = ""
) {
    @Serializable
    data class Item(
        @SerialName("itemName")
        val itemName: String = "",
        @SerialName("sku")
        val sku: List<String> = listOf()
    )
}