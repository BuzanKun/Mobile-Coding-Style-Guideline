package com.example.apiservices.data.source.network.model.request.supplier


import com.google.gson.annotations.SerializedName

data class DeleteSupplierBody(
    @SerializedName("supplierID")
    val supplierID: List<String> = emptyList()
)