package co.jonathanbernal.comerzi.datasource.storage

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FirebaseStorageManager @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    @ApplicationContext private val context: Context
) {
    private val storageRef = firebaseStorage.reference

    suspend fun uploadImageToStorage(
        path: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val fileName: String = File(path).name
        val file = context.contentResolver.openInputStream(Uri.parse(path))
        val mountainsRef = storageRef.child("${fileName}.jpg")
        val uploadTask = file?.let { mountainsRef.putStream(it) }
        uploadTask?.addOnFailureListener {
            onError(it)
        }?.addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                onSuccess(it.toString())
            }.addOnFailureListener { exception ->
                onError(exception)
            }
        }
    }
}