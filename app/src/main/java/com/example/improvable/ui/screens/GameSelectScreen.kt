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
import com.example.improvable.data.SceneInfo

// GAMES SELECT SCREEN IS THE UPDATED GAMES SCREEN IMPLEMENTATION, GAMES SCREEN JUST HAS THE LIST OF GAMES

@Composable
fun GameSelectScreen( // adding the viewmodel so we can change screen
    onNavigateBack: () -> Unit,
    viewModel: GameSelectViewModel = viewModel(
        factory = GameSelectViewModel.Factory(LocalContext.current) // factrory for viewmodel
    ),


    // 4/8 implementation, adding so we can see players and add to session
    // 4/15 removing this feature FOR NOW
    // 4/20 adding it back

    sessionsViewModel: SessionsListViewModel // Pass sessionsViewModel to add games to session
) {
    val searchText by viewModel.searchText.collectAsState() //searching by name
    val filteredGames by viewModel.filteredGames.collectAsState() // resulting list of games after filter



    // 4/8 adding so we can add players
    // 4/15 removing FOR NOW
    // 4/20 adding it back?

    val roster by viewModel.roster.collectAsState()

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
                GameSelectItem(
                    game,
                    onAddToSession = {
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

        if (showPlayerPicker && gameToAddToSession != null) {
            GameSelectPlayerSelectionDialog(
                roster = roster,
                onDismiss = {
                    showPlayerPicker = false
                    gameToAddToSession = null
                }, // dismiss --> don't worry aboyt player session
                onConfirm = { selectedPlayers ->
                    val newScene = SceneInfo(
                        game = gameToAddToSession,
                        date = System.currentTimeMillis() / 1000,
                        players = selectedPlayers,
                        notes = "",
                        thumbnailPath = "",
                        recording = ""
                    )
                    sessionsViewModel.addSceneToCurrentSession(newScene)
                    showPlayerPicker = false
                    gameToAddToSession = null
                    onNavigateBack() // Go back to the session screen
                }
            )
        }
    }
}

// used https://composeexamples.com/components/application-ui/components/accordions for guidance
@Composable // DISPLAY GAME INFORMATION
fun GameSelectItem(
    game: GamesInfo,   // pulling from json file
    onAddToSession: () -> Unit // 4/8 adding addtosession stuff
) {
    var expanded = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded.value = !expanded.value }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = game.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onAddToSession) {
                Icon(
                    imageVector = Icons.Default.Add, // BUILT IN ADD BUTTON
                    contentDescription = "Add to Session",
                    tint = MaterialTheme.colorScheme.primary // with our color scheme --> implement this further
                )
            }
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

// 4/8 ADDING PLAYER SELECTION DIALOGue
// 4/15 removing feature for now
// 4/20 ADDING IT BACK but specifically when we click the plus the check boxes come up and it works? Maybe?

@Composable
fun GameSelectPlayerSelectionDialog(
    roster: List<RosterInfo>,
    onDismiss: () -> Unit,
    onConfirm: (List<RosterInfo>) -> Unit
) {
    val selectedPlayers = remember { mutableStateListOf<RosterInfo>() }

    // https://developer.android.com/develop/ui/views/components/dialogs ref
    // the vision is that it is a pop up, you checkbox the players playing and add them and submit,
    // they're added to the sesh and then you get transported to the session screen (?)

    // 4/20 reimplementation:

    //
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Players") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (roster.isEmpty()) {
                    Text(
                        text = "No players found in roster",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(roster) { person ->
                            // 4/20 --> SELECTING ATTENDANCE PLAYER
                            GameSelectPlayerItem(
                                person = person,
                                isSelected = selectedPlayers.contains(person),
                                onToggle = { isSelected ->
                                    if (isSelected) {
                                        selectedPlayers.add(person)
                                    } else {
                                        selectedPlayers.remove(person)

                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedPlayers.toList()) }) { Text("Add to game") }
        },
        dismissButton = { // get rid of the alert popup
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
    // 4/20
    // referenced the previous attendance implementation adn modified it for this
    @Composable
    fun GameSelectPlayerItem(
        person: RosterInfo,
        isSelected: Boolean,
        onToggle: (Boolean) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle(!isSelected) }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${person.firstName} ${person.lastName}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle(it) }
        )
    }




