package com.example.shared.data.source.network.model.response.supplier

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class DeleteSupplierResponse(
    @SerialName("data")
    val `data`: JsonElement? = null,
    @SerialName("message")
    val message: String = "",
    @SerialName("status")
    val status: Int = 0
)