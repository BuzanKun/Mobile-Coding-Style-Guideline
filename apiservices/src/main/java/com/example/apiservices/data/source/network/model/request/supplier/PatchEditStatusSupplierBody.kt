package com.example.apiservices.data.source.network.model.request.supplier

import com.google.gson.annotations.SerializedName

data class PatchEditStatusSupplierBody(
    @SerializedName("supplierID")
    val supplierID: List<String> = listOf(),
    @SerializedName("status")
    val status: Boolean = false
)