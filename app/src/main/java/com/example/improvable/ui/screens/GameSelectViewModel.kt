package com.example.improvable.ui.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.improvable.data.AppPreferences
import com.example.improvable.data.GamesInfo
import com.example.improvable.data.RosterInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File

// SAME AS ORIGINAL GAME SELECT VIEW MODEL
class GameSelectViewModel(private val context: Context) : ViewModel() {
    private val _allGames = MutableStateFlow<List<GamesInfo>>(emptyList()) // games can change over time
    
    private val _searchText = MutableStateFlow("") // search can change on the inside
    val searchText = _searchText.asStateFlow() // but on the outside it is read only

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    } // i was getting mad with errors

    // WHENEVER _searchText OR _allGames CHANGE, WE CHANGE WHAT GAMES GET DISPLAYED

    val filteredGames: StateFlow<List<GamesInfo>> = _searchText
        .combine(_allGames) { text, games ->
            if (text.isBlank()) {
                games // if not searching anything THEN show all games
            } else {
                val searchInt = text.toIntOrNull()
                games.filter { game ->
                    // searching for matches
                    val matchesText = game.title.contains(text, ignoreCase = true) || // matches tag OR key word
                                     game.tags.any { tag -> tag.contains(text, ignoreCase = true) }
                    
                    val matchesPlayerCount = if (searchInt != null) { // matches how many players -> range between the min / max
                        searchInt >= game.minPlayers && (game.maxPlayers == null || searchInt <= game.maxPlayers)
                    } else false

                    matchesText || matchesPlayerCount
                }
            }
        }
        .stateIn(
            scope = viewModelScope, // start state flow
            started = SharingStarted.WhileSubscribed(5000), // keeping it alive 5 seconds after being fiddled with
            initialValue = emptyList()
        )


    private fun loadGames() {
        _allGames.value = AppPreferences.loadGamesFromPrefs(context)
    }


    // STOLEN FROM ROSTER VIEW MODEL 4/20
    private val _roster = MutableStateFlow<List<RosterInfo>>(emptyList())
    val roster = _roster.asStateFlow()

    private val rosterFile = File(context.filesDir, "rosterInfo.json") /// so we can update it


    init {
        loadGames()
        loadRoster() //load our roster
    }

    private fun loadRoster() {
        viewModelScope.launch {
            try {
                var loadedRoster: List<RosterInfo>? = null

                if (rosterFile.exists()) {
                    try {
                        val jsonString = rosterFile.readText()
                        if (jsonString.isNotBlank()) {
                            loadedRoster = json.decodeFromString<List<RosterInfo>>(jsonString)
                        }
                    } catch (e: Exception) {
                        Log.e(
                            "GameSelectViewModel",
                            "Error reading roster",
                            e
                        )
                    }
                }

                if (loadedRoster == null) {
                    try {
                        val jsonString = context.assets.open("rosterInfo.json").bufferedReader()
                            .use { it.readText() }
                        loadedRoster = json.decodeFromString<List<RosterInfo>>(jsonString)
                    } catch (e: Exception) {
                        Log.e("GameSelectViewModel", "Error loading roster from assets", e)
                    }
                }

                _roster.value = loadedRoster ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // if that data changes we update
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


    // utilized https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factoriesas a ref
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameSelectViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameSelectViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
