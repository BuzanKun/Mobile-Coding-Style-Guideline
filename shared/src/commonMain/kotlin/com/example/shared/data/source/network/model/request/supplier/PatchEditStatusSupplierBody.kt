package com.example.shared.data.source.network.model.request.supplier

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatchEditStatusSupplierBody(
    @SerialName("supplierID")
    val supplierID: List<String> = listOf(),
    @SerialName("status")
    val status: Boolean = false
)