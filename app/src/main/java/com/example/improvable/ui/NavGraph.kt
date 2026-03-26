package com.example.improvable.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.improvable.ui.screens.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { // home screen
            HomeScreen(
                onNavigateToWarmups = { navController.navigate("warmups") },
                onNavigateToGames = { navController.navigate("games") }
            )
        }
        composable("warmups") { // screen where we give warm up ideas // structure
            WarmupsScreen(
                onNavigateBack = {
                    navController.popBackStack() }
            )
        }
        composable("games") { // eventually will have list (SCROLLABLE), search function
            GamesScreen(              // NOTE FOR LATER:::: do a tag system so they can filter by tags
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun HomeScreen( // starting with three basic screens to get into the flow
    onNavigateToWarmups: () -> Unit,
    onNavigateToGames: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen")
        Button(onClick = onNavigateToWarmups) {
            Text(text = "Go to WARMUPS")
        }
        Button(onClick = onNavigateToGames) {
            Text(text = "Go to GAMEs")
        }
    }
}

@Composable
fun WarmupsScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Warm ups but SO BASIC")
        Button(onClick = onNavigateBack) {
            Text(text = "Back")
        }
    }
}

@Composable
fun GamesScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Games Sceen BASIC EDITION")
        Button(onClick = onNavigateBack) {
            Text(text = "Back")
        }
    }
}
