package com.example.apiservices.data.source.network.model.response.supplier


import com.google.gson.annotations.SerializedName

data class CreateSupplierResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
) {
    data class Data(
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("status")
        val status: Boolean = false,
        @SerializedName("companyName")
        val companyName: String = "",
        @SerializedName("country")
        val country: String = "",
        @SerializedName("state")
        val state: String = "",
        @SerializedName("companyLocation")
        val companyLocation: String = "",
        @SerializedName("companyPhoneNumber")
        val companyPhoneNumber: Long = 0,
        @SerializedName("item")
        val item: List<Item> = emptyList(),
        @SerializedName("zipCode")
        val zipCode: Int = 0,
        @SerializedName("picName")
        val picName: String = "",
        @SerializedName("picPhoneNumber")
        val picPhoneNumber: Long = 0,
        @SerializedName("picEmail")
        val picEmail: String = "",
        @SerializedName("modifiedBy")
        val modifiedBy: String = "",
        @SerializedName("createdAt")
        val createdAt: String = "",
        @SerializedName("updatedAt")
        val updatedAt: String = ""
    ) {
        data class Item(
            @SerializedName("itemName")
            val itemName: String = "",
            @SerializedName("sku")
            val sku: List<String> = listOf()
        )
    }
}