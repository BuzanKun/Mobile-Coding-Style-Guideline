package com.example.shared.base

import io.ktor.client.statement.HttpResponse

sealed class ApiResponse<out T> {
    data class Success<T>(val httpResponse: HttpResponse, val body: T) : ApiResponse<T>()
    data class Error(val exception: Exception) : ApiResponse<Nothing>()
}