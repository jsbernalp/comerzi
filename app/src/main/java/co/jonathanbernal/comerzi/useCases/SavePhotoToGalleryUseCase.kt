package co.jonathanbernal.comerzi.useCases

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SavePhotoToGalleryUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
){

    suspend fun call(capturedPhotoBitmap: Bitmap): Result<Uri> =
        withContext(Dispatchers.IO) {
            val resolver: ContentResolver = context.applicationContext.contentResolver
            val imageCollection: Uri = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                }

                else -> {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
            }
            val nowTimeStamp = System.currentTimeMillis()
            val imageContentValues: ContentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "Comerzi_${nowTimeStamp}")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.DATE_TAKEN, nowTimeStamp)
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_DCIM + "/Comerzi"
                    )
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    put(MediaStore.Images.Media.DATE_TAKEN, nowTimeStamp)
                    put(MediaStore.Images.Media.DATE_ADDED, nowTimeStamp)
                    put(MediaStore.Images.Media.DATE_MODIFIED, nowTimeStamp)
                    put(MediaStore.Images.Media.AUTHOR, "Comerzi")
                    put(MediaStore.Images.Media.DESCRIPTION, "Comerzi")
                }
            }

            val imageMediaStoreUri: Uri? = resolver.insert(imageCollection, imageContentValues)

            val result: Result<Uri> = imageMediaStoreUri?.let { uri ->
                runCatching {
                    resolver.openOutputStream(uri).use { outputStream ->
                        checkNotNull(outputStream) { "Failed to open output stream." }
                        capturedPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        imageContentValues.clear()
                        imageContentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                        resolver.update(uri, imageContentValues, null, null)
                    }

                    Result.success(uri)
                }.getOrElse { exception: Throwable ->
                    exception.message?.let(::println)
                    resolver.delete(uri, null, null)
                    Result.failure(exception)
                }
            } ?: run {
                Result.failure(Exception("Failed to create new MediaStore record."))
            }
            return@withContext result
        }
}