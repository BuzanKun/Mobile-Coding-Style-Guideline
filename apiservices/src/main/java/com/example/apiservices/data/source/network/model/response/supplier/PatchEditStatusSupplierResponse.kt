package com.example.apiservices.data.source.network.model.response.supplier

import com.google.gson.annotations.SerializedName

data class PatchEditStatusSupplierResponse(
    @SerializedName("data")
    val `data`: Any? = null,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)