package com.example.apiservices.util

import com.google.gson.JsonParseException
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeoutException

object ApiUtil {
    /**
     * Handles API errors and returns a standardized error response.
     *
     * This function takes an exception and maps it to a user-friendly error message.
     * It then creates and returns a Retrofit error response with a 500 status code
     * and the corresponding error message.
     *
     * @param exception The exception that occurred during the API call.
     * @return A Retrofit error response with a 500 status code and the error message.
     */
    fun <T> handleApiError(exception: Throwable): Response<T> {
        val message = when (exception) {
            is TimeoutException -> "Request timed out. Please try again."
            is IOException -> "Network error. Please check your connection."
            is JsonParseException -> "Malformed JSON received. Parsing failed."
            is IllegalArgumentException -> "Invalid argument provided. ${exception.message}"
            is IllegalStateException -> "Illegal application state. ${exception.message}"
            else -> "Unexpected error occurred: ${exception.message}"
        }
        return Response.error(500, message.toResponseBody())
    }
}
