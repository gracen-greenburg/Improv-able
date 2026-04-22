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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.improvable.data.AppPreferences
import com.example.improvable.ui.screens.GamesScreen
import com.example.improvable.ui.screens.HomeScreen
import com.example.improvable.ui.screens.RosterScreen
import com.example.improvable.ui.screens.SceneScreen
import com.example.improvable.ui.screens.SessionScreen
import com.example.improvable.ui.screens.SessionsListViewModel
import com.example.improvable.ui.screens.SessionsScreen
import com.example.improvable.ui.screens.SuggestionsScreen
import com.example.improvable.ui.screens.WarmupsScreen
import com.example.improvable.ui.screens.GameSelectScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val sessionsViewModel: SessionsListViewModel = viewModel(
        factory = SessionsListViewModel.Factory(LocalContext.current)
    )

    // Initialize all app data (only if data has never been initialized on the app_.
    AppPreferences.checkInit(LocalContext.current)

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { // home screen
            HomeScreen(
                onNavigateToWarmups = { navController.navigate("warmups") },
                onNavigateToGames = { navController.navigate("games") },
                onNavigateToLeadership = { navController.navigate("leadership") },
                onNavigateToRoster = { navController.navigate("roster") },
                onNavigateToRecording = { navController.navigate("recording") },
                onNavigateToSuggestions = { navController.navigate("suggestions")},
                onNavigateToSessions = { navController.navigate("sessions")}
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
        composable("leadership") {
            LeadershipScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("roster") {
            RosterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("suggestions") {
            SuggestionsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("sessions") {
            SessionsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToScenes = {
                    navController.navigate("scenes")
                },
                viewModel = sessionsViewModel
            )
        }
        composable("scenes") {
            SessionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToScene = {
                    navController.navigate("scene")
                },
                onNavigateToGameSelect = {
                    navController.navigate("game_select")
                },
                viewModel = sessionsViewModel
            )
        }
        composable("scene") {
            SceneScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = sessionsViewModel
            )
        }
        composable("game_select") {
            GameSelectScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                sessionsViewModel = sessionsViewModel
            )
        }
    }
}

@Composable
fun HomeScreen( // starting with three basic screens to get into the flow
    onNavigateToWarmups: () -> Unit,
    onNavigateToGames: () -> Unit,
    onNavigateToLeadership: () -> Unit,
    onNavigateToRoster: () -> Unit,
    onNavigateToRecording: () -> Unit, // added this back since it's used in HomeScreen
    onNavigateToSuggestions: () -> Unit,
    onNavigateToSessions: () -> Unit) {

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
        Button(onClick = onNavigateToLeadership) {
            Text(text = "Leader screen info and such")
        }
        Button(onClick = onNavigateToRoster) {
            Text(text = "Look at my improv roster")
        }
        Button(onClick = onNavigateToSuggestions) {
            Text(text = "CAN I GET A SUGGESTION")
        }
        Button(onClick = onNavigateToSessions) {
            Text(text = "SESSIONS")
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

//@Composable --> going to remove search screen, it can be accomplished with just games screen.
//fun SearchScreen(onNavigateBack: () -> Unit) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Oh I'm searching it I'm searching it")
//        Button(onClick = onNavigateBack) {
//            Text(text = "Back")
//        }
//    }
//}

@Composable
fun LeadershipScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Oh and we're leading it")
        Button(onClick = onNavigateBack) {
            Text(text = "Back")
        }
    }
}

//@Composable --. commented out vcbc it wouldn't let the updated screen work
//fun RosterScreen(onNavigateBack: () -> Unit) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Here we're going to have a scrollable addable list of people with like " +
//                "\n checkmarks to mark presence, easy adding \n screen, and it is kept as data")
//        Button(onClick = onNavigateBack) {
//            Text(text = "Back")
//        }
//    }
//}

