package co.jonathanbernal.comerzi.ui.screen.common

import android.Manifest
import android.os.Build


val permissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_MEDIA_IMAGES
    )
} else {
    listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}
