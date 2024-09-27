package co.jonathanbernal.comerzi.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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


fun<T> Task<T>.snapshotFlow(): Flow<T> =
    callbackFlow {
        val listenerRegistration = addOnSuccessListener {
            trySend(it)
        }
        addOnFailureListener {
            close()
            return@addOnFailureListener
        }
        awaitClose {
            listenerRegistration.addOnCanceledListener {
                close()
            }
        }
    }

class ApiException(message: String, cause: Throwable) : Exception(message, cause)