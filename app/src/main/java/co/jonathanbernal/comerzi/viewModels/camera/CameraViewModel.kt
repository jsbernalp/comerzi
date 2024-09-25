package co.jonathanbernal.comerzi.viewModels.camera

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.jonathanbernal.comerzi.useCases.SavePhotoToGalleryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cameraUseCase: SavePhotoToGalleryUseCase
) : ViewModel() {

    private val _photo = MutableStateFlow<Bitmap?>(null)
    val photo: StateFlow<Bitmap?> = _photo.asStateFlow()


    fun getApplicationContext(): Context {
        return context
    }

    fun updateTakePhoto(bitmap: Bitmap?) {
        _photo.value = bitmap
    }

    fun storePhotoInGallery(onGetUri: (Uri) -> Unit) {
        viewModelScope.launch {
            val bitmap = _photo.value ?: return@launch
            cameraUseCase.call(bitmap)
                .fold(
                    onSuccess = { uri -> onGetUri.invoke(uri)},
                    onFailure = {}
                )
        }
    }

}