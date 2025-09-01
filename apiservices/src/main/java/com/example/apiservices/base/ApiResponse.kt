package com.example.apiservices.base

import com.google.gson.annotations.SerializedName

/**
 * A generic class that represents an API response.
 *
 * @param T The type of the data contained in the response.
 * @property data The data returned by the API. It can be null.
 * @property message A message associated with the response, typically used for error messages.
 * @property status The status code of the response.
 */
data class ApiResponse<T>(
    @SerializedName("data")
    val data: T?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0,
)