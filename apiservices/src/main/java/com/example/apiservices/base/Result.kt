package com.example.apiservices.base

/**
 * A sealed class that represents the result of an operation, which can be either a success or an error.
 *
 * @param T The type of the data contained in the result.
 * @property isSuccess Indicates whether the result is a success.
 */
sealed class Result<T>(val isSuccess: Boolean) {
    /**
     * A subclass of Result that represents a successful operation.
     *
     * @param T The type of the data contained in the result.
     * @property data The data returned by the successful operation.
     */
    class Success<T>(val data: T) : Result<T>(isSuccess = true)

    /**
     * A subclass of Result that represents a failed operation.
     *
     * @param T The type of the data contained in the result.
     * @property message An optional message describing the error.
     */
    class Error<T>(val message: String?) : Result<T>(isSuccess = false)
}