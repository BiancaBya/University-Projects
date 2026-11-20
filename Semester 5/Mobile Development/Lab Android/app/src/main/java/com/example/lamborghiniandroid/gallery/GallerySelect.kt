package com.example.lamborghiniandroid.gallery

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

@Composable
fun GallerySelect(
    modifier: Modifier,
    onImageUri: (Uri) -> Unit = { }
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageUri(uri ?: EMPTY_IMAGE_URI)
        }
    )

    SideEffect {
        launcher.launch("image/*")
    }
}
