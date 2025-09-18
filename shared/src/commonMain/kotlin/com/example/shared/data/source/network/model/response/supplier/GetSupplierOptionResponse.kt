package com.example.shared.data.source.network.model.response.supplier

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSupplierOptionResponse(
    @SerialName("data")
    val `data`: Data = Data(),
    @SerialName("message")
    val message: String = "",
    @SerialName("status")
    val status: Int = 0
) {
    @Serializable
    data class Data(
        @SerialName("supplierOption")
        val supplierOption: List<OptionData> = listOf(),
        @SerialName("cityOption")
        val cityOption: List<OptionData> = listOf(),
        @SerialName("itemNameOption")
        val itemNameOption: List<OptionData> = listOf(),
        @SerialName("modifiedByOption")
        val modifiedByOption: List<OptionData> = listOf(),
    ) {
        @Serializable
        data class OptionData(
            @SerialName("label")
            val label: String = "",
            @SerialName("value")
            val value: String = ""
        )
    }
}