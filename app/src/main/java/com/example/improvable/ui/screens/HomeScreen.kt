package com.example.improvable.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    // structure: i want warmup screen, list of games // search screen,
    // leadership stuff, roster of people, recording // transcription function to store,
    // and a suggestions page

    // 3/26 starting small with 2 basic screens (no searching yet)
    onNavigateToWarmups: () -> Unit,
    onNavigateToGames: () -> Unit,
    onNavigateToLeadership: () -> Unit,
    onNavigateToRoster: () -> Unit,
    onNavigateToRecording: () -> Unit,
    onNavigateToSuggestions: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onNavigateToWarmups) {
            Text("Warmups")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToGames) {
            Text("Games")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToSearch) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToLeadership) {
            Text("Leadership")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToRoster) {
            Text("Roster")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToSuggestions) {
            Text("Suggestions")
        }
    }
}
