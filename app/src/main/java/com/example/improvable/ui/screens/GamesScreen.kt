package com.example.improvable.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon // FOR THE ADD BUTTON
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.improvable.data.GamesInfo
import com.example.improvable.data.RosterInfo

@Composable
fun GamesScreen( // adding the viewmodel so we can change screen
    onNavigateBack: () -> Unit,
    viewModel: GamesViewModel = viewModel(
        factory = GamesViewModel.Factory(LocalContext.current) // factrory for viewmodel
    ),
    // 4/8 implementation, adding so we can see players and add to session
    rosterViewModel: RosterViewModel = viewModel(
        factory = RosterViewModel.Factory(LocalContext.current)
    )
) {
    val searchText by viewModel.searchText.collectAsState() //searching by name
    val filteredGames by viewModel.filteredGames.collectAsState() // resulting list of games after filter
    // 4/8 adding so we can add players
    val roster by rosterViewModel.roster.collectAsState()

    var showPlayerPicker by remember { mutableStateOf(false) } // pick players in game
    var gameToAddToSession by remember { mutableStateOf<GamesInfo?>(null) } // add the game to sesh

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) { // search bar
        OutlinedTextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChange, // CHANGE ON VALUE
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search games or tags...") },
            label = { Text("Search") }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            items(filteredGames) { game ->
                // 4/8 --> making an add to session thing
                GameItem(
                    game,
                    onAddToSession = {
                        // IMPLEMENT LOGIC TO OPEN PLAYERS AND ADD TO SESH
                        gameToAddToSession = game
                        showPlayerPicker = true
                    }
                )
                HorizontalDivider()
            }
        }

        Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Back") // we can still click back
        }
    }
}

// used https://composeexamples.com/components/application-ui/components/accordions for guidance
@Composable // DISPLAY GAME INFORMATION
fun GameItem(
    game: GamesInfo,   // pulling from json file
    onAddToSession: () -> Unit // 4/8 adding addtosession stuff
    ) {
    var expanded = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded.value = expanded.value.not() }
    ) { // 4/8 --> adding a plus button to the game so we can add to session?
       // Text(text = game.title, style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text( // game title text
                text = game.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
    AnimatedVisibility(
        visible = expanded.value,
        enter = expandVertically(
            spring(
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = IntSize.VisibilityThreshold
            )
        ),
        exit = shrinkVertically()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            //display the number of players differently based on if the number is a range, single value, or minimum.
            var playerCountText = "Players: ${game.minPlayers}"
            if (game.maxPlayers == null) {
                playerCountText += "+"
            } else if (game.minPlayers != game.maxPlayers) {
                playerCountText += " - ${game.maxPlayers}"
            }
            Text(text = playerCountText)
            // display the game's description.
            Text(text = game.description, style = MaterialTheme.typography.bodyMedium)
            if (game.tags.isNotEmpty()) {
                Text(
                    text = "Tags: ${game.tags.joinToString(", ")}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

