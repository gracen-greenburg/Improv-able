package com.example.improvable.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.improvable.R
import java.io.File
import java.io.FileOutputStream
import java.util.Date

@Composable
fun SceneScreen(
    onNavigateBack: () -> Unit, // same thing as gameScreen
    viewModel: SessionsListViewModel = viewModel(
        factory = SessionsListViewModel.Factory(LocalContext.current)
    )
) {
    val currentScene = viewModel.currentScene
    var tempNotes by remember { mutableStateOf(currentScene.notes) }

    // using bit map for camera 4/15
    // we decode it into the image for the thumbnail
    var bitmap by remember {
        mutableStateOf<Bitmap?>(
            if (currentScene.thumbnailPath.isNotEmpty()) {
                BitmapFactory.decodeFile(currentScene.thumbnailPath)
            } else {
                null
            }
        )
    }

    // 4/15 we need context for all camera data
    val context = LocalContext.current

    // LAUNCHING THE CAMERA 4/15
    // https://developer.android.com/jetpack/androidx/releases/camera

    // --> delegating the actual camera functioning to the device itself
    // https://developer.android.com/media/camera/camera-deprecated/photobasics
    // bitmapping it
    // https://developer.android.com/topic/performance/graphics/manage-memory#save-a-bitmap-for-later-use
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { b: Bitmap? ->
        // so if there is a bitmap, we save it
        if (b != null) {
            bitmap = b
            val path = saveBitmap(context, b, "scene_${currentScene.date}.jpg") // linking photo data with date n such
            currentScene.thumbnailPath = path
        }
    }

    // Andorid studio needs permission --> if we don't have it, we ask
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (currentScene.game == null) "Unknown Game" else currentScene.game!!.title)
        Text(Date(currentScene.date * 1000).toString()) // real tiny
        
        Spacer(modifier = Modifier.height(8.dp))

        if (bitmap != null) { // if there is a bitmap that can be made into an image, we do it
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "Scene thumbnail",
                modifier = Modifier.size(200.dp)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.plus_sign),
                contentDescription = "No thumbnail",
                modifier = Modifier.size(200.dp)
            )
        }

        // BUTTON FOR PERMISSIONS
        Button(onClick = {
            val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch()
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text("Take Picture")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(currentScene.players) { player ->
                Text(player.firstName + " " + player.lastName)
            }
        }

        // Notes --> Edited 4/15 to fit with camera integration adn game select
        Text("Notes")
        TextField(
            value = tempNotes,
            onValueChange = { 
                tempNotes = it
                currentScene.notes = it
            },
        )

        Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back")
        }
    }
}

// https://developer.android.com/topic/performance/graphics/manage-memory#save-a-bitmap-for-later-use
// https://stackoverflow.com/questions/649154/save-bitmap-to-location#:~:text=I%20know%20this%20question%20is,this)%20close()%20%7D%20%7D%20%7D
// ESPECIALLY THIS https://dpw-developer.medium.com/simple-steps-to-saving-loading-and-deleting-bitmaps-in-android-storage-using-java-a974b9d97c4a
// --> use snippet, and instead of unique name generated we use the context assigned to the bitmap, which does make it unique
private fun saveBitmap(context: Context, bitmap: Bitmap, name: String): String {
    val file = File(context.filesDir, name)
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return file.absolutePath
}
