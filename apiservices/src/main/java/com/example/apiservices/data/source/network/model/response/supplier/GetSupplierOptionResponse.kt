package com.example.apiservices.data.source.network.model.response.supplier


import com.google.gson.annotations.SerializedName

data class GetSupplierOptionResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
) {
    data class Data(
        @SerializedName("supplierOption")
        val supplierOption: List<OptionData> = listOf(),
        @SerializedName("cityOption")
        val cityOption: List<OptionData> = listOf(),
        @SerializedName("itemNameOption")
        val itemNameOption: List<OptionData> = listOf(),
        @SerializedName("modifiedByOption")
        val modifiedByOption: List<OptionData> = listOf(),
    ) {
        data class OptionData(
            @SerializedName("label")
            val label: String = "",
            @SerializedName("value")
            val value: String = ""
        )
    }
}