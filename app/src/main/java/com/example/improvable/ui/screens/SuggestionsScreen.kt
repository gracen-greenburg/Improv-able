package com.example.improvable.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
//            .fillMaxSize()
//            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header("Suggestions")

        val newSuggestion = remember {mutableStateOf("")}

        fun showQuestion() {
            answer.value = viewModel.getQuestion()
        }

        fun showSuggestion() {
            answer.value = viewModel.getSuggestion()
        }


        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text("Click to get a question for the audience to come up with a guided suggestion.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                showQuestion()
            }, modifier = Modifier
                .padding(top = 4.dp)
                .size(height = 40.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )) {
                Text("Get Question",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }
            Spacer(modifier = Modifier.height(32.dp))

            Text("Click to get a random suggestion")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                showSuggestion()
            }, modifier = Modifier
                .padding(top = 4.dp)
                .size(height = 40.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )) {
                Text("Get Suggestion",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }

            Spacer(modifier = Modifier.weight(1f))


            Box(modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)) {
                Text(answer.value, modifier = Modifier.padding(30.dp), fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text("Or, add your own suggestion to the list of random suggestions.")
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = newSuggestion.value,
                onValueChange = { newSuggestion.value = it },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.addSuggestion(newSuggestion.value)
                newSuggestion.value = ""
            }, modifier = Modifier
                .padding(top = 4.dp)
                .size(height = 40.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )) {
                Text("Add to list",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onNavigateBack,
                 modifier = Modifier
                    .padding(top = 4.dp)
                    .size(height = 40.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Back",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }
        }
    }
}

