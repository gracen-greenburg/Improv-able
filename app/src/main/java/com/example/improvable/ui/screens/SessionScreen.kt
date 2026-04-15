package com.example.improvable.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.improvable.data.SceneInfo
import com.example.improvable.data.SessionInfo
import com.example.improvable.R
import java.util.Date

@Composable
fun SessionScreen(
    onNavigateBack: () -> Unit, // same thing as gameScreen
    onNavigateToScene: () -> Unit,
    viewModel: SessionsListViewModel = viewModel(
        factory = SessionsListViewModel.Factory(LocalContext.current)
    )
) {
    val currentSession = viewModel.currentSession
    var tempNotes = remember {mutableStateOf(currentSession.notes)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(currentSession.scenes.size + 1) {sceneIndex ->
                if (sceneIndex < currentSession.scenes.size) {
                    SceneThumbnail(currentSession.scenes[sceneIndex], viewModel, onNavigateToScene)
                }
                else {
                    AddSceneThumbnail(viewModel, onNavigateToScene)
                }
            }
        }

        Text("Notes")
        TextField(
            value = tempNotes.value,
            onValueChange = { tempNotes.value = it },
        )

        Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back")
        }
    }
}

@Composable
fun SceneThumbnail(scene : SceneInfo, viewModel: SessionsListViewModel, onNavigateToScene: () -> Unit) {
    val image = painterResource(R.drawable.unloaded_image_icon)
    Box(
        modifier = Modifier
            .clickable{
                viewModel.currentScene = scene
                onNavigateToScene()
            }
    ) {
        Image(image, "The thumbnail image for a scene.")
        Text(if (scene.game == null) "New Scene" else scene.game!!.title , modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun AddSceneThumbnail(viewModel: SessionsListViewModel, onNavigateToScene: () -> Unit) {
    val image = painterResource(R.drawable.plus_sign)
    Box(
            modifier = Modifier
                .clickable{
                    //go to the game selection screen, then after that navigate to SceneScreen.
                }
            ) {
        Image(image, "The thumbnail image for a scene.")
        Text("Add Scene", modifier = Modifier.align(Alignment.BottomCenter))
    }
}
