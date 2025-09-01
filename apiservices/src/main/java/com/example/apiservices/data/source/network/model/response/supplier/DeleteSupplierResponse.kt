package com.example.apiservices.data.source.network.model.response.supplier


import com.google.gson.annotations.SerializedName

data class DeleteSupplierResponse(
    @SerializedName("data")
    val `data`: Int? = null,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)