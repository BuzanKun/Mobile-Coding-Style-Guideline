package com.example.apiservices.data.source.network.model.request.supplier


import com.google.gson.annotations.SerializedName

data class CreateUpdateSupplierBody(
    @SerializedName("companyName")
    val companyName: String = "",
    @SerializedName("item")
    val item: List<Item> = emptyList(),
    @SerializedName("country")
    val country: String = "",
    @SerializedName("state")
    val state: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("zipCode")
    val zipCode: String = "",
    @SerializedName("companyLocation")
    val companyLocation: String = "",
    @SerializedName("companyPhoneNumber")
    val companyPhoneNumber: String = "",
    @SerializedName("picName")
    val picName: String = "",
    @SerializedName("picPhoneNumber")
    val picPhoneNumber: String = "",
    @SerializedName("picEmail")
    val picEmail: String = ""
) {
    data class Item(
        @SerializedName("itemName")
        val itemName: String = "",
        @SerializedName("sku")
        val sku: List<String> = listOf()
    )
}