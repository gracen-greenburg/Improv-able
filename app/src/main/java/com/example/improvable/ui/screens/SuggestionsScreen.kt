package com.example.improvable.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SuggestionsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SuggestionsViewModel = viewModel(
        factory = SuggestionsViewModel.Factory(LocalContext.current)
    )) {
    var answer = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val newSuggestion = remember {mutableStateOf("")}

        fun showQuestion() {
            var old = answer.value;
            do {
                answer.value = viewModel.getQuestion()
            } while (old.equals(answer.value))
        }

        fun showSuggestion() {
            var old = answer.value;
            do {
                answer.value = viewModel.getSuggestion()
            } while (old.equals(answer.value))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Suggestions", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Text("Click to get a question for the audience to come up with a guided suggestion.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            showQuestion()
        }) {
            Text("Get Question")
        }
        Spacer(modifier = Modifier.height(32.dp))

        Text("Click to get a random suggestion")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            showSuggestion()
        }) {
            Text("Get Suggestion")
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(answer.value)

        Spacer(modifier = Modifier.weight(1f))

        Text("Or, add your own suggestion to the list of random suggestions.")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = newSuggestion.value,
            onValueChange = { newSuggestion.value = it },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) {
            Text("Add to list")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}

