package com.example.improvable.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.improvable.R
import java.util.Date

@Composable
fun SceneScreen(
    onNavigateBack: () -> Unit, // same thing as gameScreen
    viewModel: SessionsListViewModel = viewModel(
        factory = SessionsListViewModel.Factory(LocalContext.current)
    )
) {
    val currentScene = viewModel.currentScene
    var tempNotes = remember {mutableStateOf(currentScene.notes)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (currentScene.game == null) "Unknown Game" else currentScene.game!!.title)
        Text(Date(currentScene.date * 1000).toString())
        Image(painterResource(R.drawable.plus_sign), "The thumbnail image for this scene.")
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(currentScene.players) { player ->
                Text(player.firstName + " " + player.lastName)
            }
        }

        Text("Notes")
        TextField(
            value = tempNotes.value,
            onValueChange = { tempNotes.value = it
                              currentScene.notes = tempNotes.value},
        )

        Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back")
        }
    }
}
