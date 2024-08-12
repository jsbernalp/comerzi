package co.jonathanbernal.comerzi.utils

import retrofit2.Response


fun <T> Response<T>.toMapResult(): Result<T> {
    return try {
        // Llamada a la API
        if (this.isSuccessful) {
            Result.success(
                this.body() ?: throw IllegalStateException("Response body is null")
            )
        } else {
            Result.failure(
                ApiException(
                    "API error",
                    IllegalStateException("Response body is null")
                )
            )
        }
    } catch (e: Exception) {
        Result.failure(ApiException("API error: ${e.message}", e))
    }
}

class ApiException(message: String, cause: Throwable) : Exception(message, cause)