package com.example.improvable.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.improvable.data.SessionInfo
import java.util.Date

@Composable
fun SessionsScreen(
    onNavigateBack: () -> Unit, // same thing as gameScreen
    onNavigateToScenes: () -> Unit,
    viewModel: SessionsListViewModel = viewModel(
        factory = SessionsListViewModel.Factory(LocalContext.current)
    )
) {
    val displayedSessions by viewModel.displayedSessions.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        //NOTE: make this button add a new Session and take the user to the SessionScreen for that session.
        Button(onClick = {
            val newSesh = SessionInfo(emptyList(), System.currentTimeMillis()/1000, "")
            viewModel.setCurSesh(newSesh)
            onNavigateToScenes()
        }) {
            Text("Add New")
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(displayedSessions) { session ->
                SessionItem(session, viewModel, onNavigateToScenes)
                HorizontalDivider()
            }
        }

        Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back")
        }
    }
}

@Composable
fun SessionItem(session : SessionInfo,
                viewModel : SessionsListViewModel,
                onNavigateToScenes: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable{
                viewModel.setCurSesh(session)
                onNavigateToScenes()
            }
    ) {
        val date = Date(session.date * 1000)
        Text(text = date.toString(), style = MaterialTheme.typography.titleLarge)
    }
}