package com.example.improvable.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.Image
import androidx.compose.material3.Surface
import androidx.compose.ui.res.painterResource
import com.example.improvable.R
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight



@Composable
fun HomeScreen(
    // structure: i want warmup screen, list of games // search screen,
    // leadership stuff, roster of people, recording // transcription function to store,
    // and a suggestions page

    // 3/26 starting small with 2 basic screens (no searching yet)
    // 3/29 implemented search on games screen
    // 4/21 Working on design

    onNavigateToWarmups: () -> Unit,
    onNavigateToGames: () -> Unit,
    onNavigateToSessions: () -> Unit,
    onNavigateToRoster: () -> Unit,
    onNavigateToSuggestions: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        @Composable
        fun DisplayLocalImage() {
            Image(
                painter = painterResource(id = R.drawable.overreactors_logo),
                contentDescription = "Descriptive text for accessibility",
                modifier = Modifier.size(height = 275.dp, width = 275.dp)
            )
        }

        DisplayLocalImage()

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToWarmups,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(height = 75.dp, width = 1000.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ){
            Text("Warmups",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onNavigateToGames,
                modifier = Modifier.size(height = 200.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )) {
                Text("Games",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onNavigateToSessions,
                modifier = Modifier.size(height = 200.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )) {
                Text("Sessions",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onNavigateToRoster,
                modifier = Modifier.size(height = 200.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )) {
                Text("Roster",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onNavigateToSuggestions,
                modifier = Modifier.size(height = 200.dp, width = 175.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
                ) {
                Text("Suggestions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun Header(title: String, subtitle: String = "") {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                // .fillMaxSize() --> parent column stole this and made everything the header which was annoying
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (subtitle.isEmpty()) Spacer(modifier = Modifier.height(15.dp))
            Text(
                title,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(if (subtitle.isEmpty()) 15.dp else 10.dp))
            if (subtitle.isNotEmpty()) {
                Text(
                    subtitle,
                    fontSize = 20.sp
                    // fontWeight = FontWeight.Light
                ) // real tiny
            }
        }
    }
}