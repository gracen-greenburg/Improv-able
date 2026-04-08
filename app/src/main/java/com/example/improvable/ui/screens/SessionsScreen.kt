package com.example.improvable.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun SessionsScreen(
    onNavigateBack: () -> Unit, // same thing as gameScreen
    viewModel: SessionsViewModel = viewModel(
        factory = SessionsViewModel.Factory(LocalContext.current)
    )
) {
    val displayedSessions by viewModel.displayedSessions.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            items(displayedSessions) { session ->
                SessionItem(session)
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun SessionItem(session : SessionInfo){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable{}
    ) {
        Text(text = session.date.toString(), style = MaterialTheme.typography.titleLarge)
    }
}