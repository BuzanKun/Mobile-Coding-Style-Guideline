package com.example.shared.data.source.network.model.response.supplier

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSupplierResponse(
    @SerialName("data")
    val `data`: Data? = null,
    @SerialName("message")
    val message: String = "",
    @SerialName("status")
    val status: Int = 0
) {
    @Serializable
    data class Data(
        @SerialName("totalRecords")
        val totalRecords: Int = 0,
        @SerialName("data")
        val `data`: List<Data> = listOf()
    ) {
        @Serializable
        data class Data(
            @SerialName("_id")
            val id: String = "",
            @SerialName("companyName")
            val companyName: String = "",
            @SerialName("item")
            val item: List<Item> = listOf(),
            @SerialName("country")
            val country: String = "",
            @SerialName("state")
            val state: String = "",
            @SerialName("city")
            val city: String = "",
            @SerialName("picName")
            val picName: String = "",
            @SerialName("status")
            val status: Boolean = false,
            @SerialName("modifiedBy")
            val modifiedBy: String = "",
            @SerialName("created_at")
            val createdAt: String = "",
            @SerialName("updated_at")
            val updatedAt: String = ""
        ) {
            @Serializable
            data class Item(
                @SerialName("_id")
                val id: String = "",
                @SerialName("supplier_id")
                val supplierId: String = "",
                @SerialName("itemName")
                val itemName: String = "",
                @SerialName("sku")
                val sku: List<String> = listOf()
            )
        }
    }
}