package com.example.shared.util

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

object ApiUtil {
    fun handleApiError(exception: Throwable): NetworkException {
        return when (exception) {
            is HttpRequestTimeoutException -> NetworkException.Timeout
            is IOException -> NetworkException.NoConnection
            is SerializationException -> NetworkException.Deserialization
            is ClientRequestException -> NetworkException.Client(exception.response.status.value)
            is ServerResponseException -> NetworkException.Server(exception.response.status.value)
            else -> NetworkException.Unknown(exception)
        }
    }
}

sealed class NetworkException(override val message: String) : Exception(message) {
    data object Timeout : NetworkException("Request timed out. Please try again.")
    data object NoConnection : NetworkException("Network error. Please check your connection.")
    data object Deserialization : NetworkException("A problem occurred while parsing the response.")
    data class Server(val code: Int) : NetworkException("Server error: $code")
    data class Client(val code: Int) : NetworkException("Client error: $code")
    data class Unknown(val throwable: Throwable) : NetworkException("An unexpected error occurred.")
}