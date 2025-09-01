package com.example.apiservices.data.source.network.model.response.supplier


import com.google.gson.annotations.SerializedName

data class GetSupplierByIdResponse(
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
        @SerializedName("companyName")
        val companyName: String = "",
        @SerializedName("item")
        val item: List<Item> = listOf(),
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
        val picEmail: String = "",
        @SerializedName("status")
        val status: Boolean = false,
        @SerializedName("modifiedBy")
        val modifiedBy: String = "",
        @SerializedName("created_at")
        val createdAt: String = "",
        @SerializedName("updated_at")
        val updatedAt: String = ""
    ) {
        data class Item(
            @SerializedName("_id")
            val id: String = "",
            @SerializedName("supplier_id")
            val supplierId: String = "",
            @SerializedName("itemName")
            val itemName: String = "",
            @SerializedName("sku")
            val sku: List<String> = listOf()
        )
    }
}