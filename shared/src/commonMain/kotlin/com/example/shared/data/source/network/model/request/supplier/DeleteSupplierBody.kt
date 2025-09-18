package com.example.shared.data.source.network.model.request.supplier

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteSupplierBody(
    @SerialName("supplierID")
    val supplierID: List<String> = emptyList()
)