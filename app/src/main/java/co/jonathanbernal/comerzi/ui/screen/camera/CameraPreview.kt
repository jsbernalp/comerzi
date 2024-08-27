package co.jonathanbernal.comerzi.ui.screen.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import co.jonathanbernal.comerzi.ui.screen.common.permissionList
import co.jonathanbernal.comerzi.viewModels.camera.CameraViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreview(
    innerPadding: PaddingValues,
    onUriChange: (Uri) -> Unit
) {
    val cameraPermissionState =
        rememberMultiplePermissionsState(permissions = permissionList)

    LaunchedEffect(Unit) {
        cameraPermissionState.launchMultiplePermissionRequest()
    }

    val cameraViewModel = hiltViewModel<CameraViewModel>()
    val applicationContext = cameraViewModel.getApplicationContext()
    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        val currentPhoto by cameraViewModel.photo.collectAsState()
        currentPhoto?.let { photo ->
            ImageCaptured(
                onUriChange = onUriChange,
                modifier = Modifier.fillMaxSize(),
                bitmap = photo,
                cameraViewModel = cameraViewModel
            )
        } ?: run {
            Camera(cameraViewModel, controller, applicationContext)
        }
    }
}

@Composable
fun Camera(
    cameraViewModel: CameraViewModel,
    controller: LifecycleCameraController,
    applicationContext: Context
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val lifecycleOwner = LocalLifecycleOwner.current
        AndroidView(
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )
        IconButton(
            onClick = {
                controller.cameraSelector =
                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
            },
            modifier = Modifier.offset(16.dp, 16.dp)

        ) {
            Icon(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = "Switch camera"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = {
                    takePhoto(controller, applicationContext) { bitmap ->
                        Log.e("CameraPreview", "Photo taken $bitmap")
                        cameraViewModel.updateTakePhoto(bitmap)
                    }
                },
                colors = IconButtonColors(Color.White, Color.Black, Color.White, Color.White),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Take picture"
                )
            }
        }
    }
}

@Composable
fun ImageCaptured(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    cameraViewModel: CameraViewModel,
    onUriChange: (Uri) -> Unit
) {
    Box(modifier = modifier) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Image captured",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                cameraViewModel.storePhotoInGallery() { uri ->
                    onUriChange.invoke(uri)

                }
            }) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = Icons.Default.SaveAlt,
                    contentDescription = "Guardar"
                )
                Text(text = "Guardar")
            }

            Button(
                onClick = { cameraViewModel.updateTakePhoto(null) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Cancelar"
                )
                Text(text = "Cancelar")
            }
        }
    }
}


fun takePhoto(
    controller: LifecycleCameraController,
    applicationContext: Context,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(applicationContext),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                onPhotoTaken(image.toBitmap().rotateBitmap(image.imageInfo.rotationDegrees))
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("MainActivity", "Image capture failed", exception)
            }
        }
    )
}

fun Bitmap.rotateBitmap(rorationDegress: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rorationDegress.toFloat())
        postScale(-1f, -1f)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}