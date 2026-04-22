package com.example.improvable.ui.screens

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.improvable.data.SceneInfo
import com.example.improvable.R
import java.util.Date

const val thumbnailScale = 0.7f

@Composable
fun SessionScreen(
    onNavigateBack: () -> Unit, // same thing as gameScreen
    onNavigateToScene: () -> Unit,
    onNavigateToGameSelect: () -> Unit,
    viewModel: SessionsListViewModel
) {
    val currentSession = viewModel.currentSession ?: return
    var tempNotes by remember(currentSession) { mutableStateOf(currentSession.notes) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Session: ${Date(currentSession.date * 1000)}",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(currentSession.scenes) { scene ->
                SceneThumbnail(scene, viewModel, onNavigateToScene)
            }
            item {
                AddSceneThumbnail(onNavigateToGameSelect)
            }
        }

        Text("Notes")
        TextField(
            // 4/15 I keep getting errors here. Tried implementing by adding current Session notes as it as well
            value = tempNotes,
            onValueChange = {
                tempNotes = it
                currentSession.notes = it
            },
            modifier = Modifier.fillMaxWidth()

        )
        Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Back")
        }
    }
}

@Composable
fun SceneThumbnail(scene : SceneInfo, viewModel: SessionsListViewModel, onNavigateToScene: () -> Unit) {
    val bitmap = remember(scene.thumbnailPath) {
        if (scene.thumbnailPath.isNotEmpty()) {
            BitmapFactory.decodeFile(scene.thumbnailPath)
        } else {
            null
        }
    }

    Box(
        modifier = Modifier
            .clickable {
                viewModel.currentScene = scene
                onNavigateToScene()
            },
        contentAlignment = Alignment.Center
    ) {
        //IMAGE HANDLING
                // bit maps --> make bitmap from scene  into image for thumbnail
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Scene thumbnail",
                modifier = Modifier.fillMaxSize().scale(thumbnailScale),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.unloaded_image_icon),
                contentDescription = "No thumbnail",
                modifier = Modifier.scale(thumbnailScale)
            )
        }
        Text(
            text = if (scene.game == null) "New Scene" else scene.game!!.title,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
// adjusting to get game select screen navigation in there 4/15
fun AddSceneThumbnail(onNavigateToGameSelect: () -> Unit) {
    val image = painterResource(R.drawable.plus_sign)
    Box(
            modifier = Modifier
                .clickable{
                    //go to the game selection screen, then after that navigate to SceneScreen.
                    onNavigateToGameSelect() // added 4/15
                }
            ) {
        Image(image, "The thumbnail image for a scene.", modifier = Modifier.scale(thumbnailScale))
        Text("Add Scene", modifier = Modifier.align(Alignment.BottomCenter))
    }
}
