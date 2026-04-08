package com.example.improvable.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.improvable.data.WarmupGamesInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

// Warmups View Model --> 4/7 implementation, very similar to RosterViewModel and GamesViewModel w/o bells and whistles
// 4/8 added search filter like games screen
class WarmupsViewModel(private val context: Context) : ViewModel() {
    private val _warmupGames = MutableStateFlow<List<WarmupGamesInfo>>(emptyList())
    val warmupGames: StateFlow<List<WarmupGamesInfo>> = _warmupGames.asStateFlow()
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // yoinked filter feature from gamesviewmodel
    val filteredGames: StateFlow<List<WarmupGamesInfo>> = _searchText
        .combine(_warmupGames) { text, games ->
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

    init {
        loadWarmupGames() // load in games
    }

    // same function as gamesviewmodel
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    // FUTURE:
    // implementation where it can suggest a random warmup game as a sort of popup

    private fun loadWarmupGames() {
        viewModelScope.launch {
            try {
                val jsonString = context.assets.open("warmupGamesInfo.json").bufferedReader().use { it.readText() }
                _warmupGames.value = Json.decodeFromString<List<WarmupGamesInfo>>(jsonString)
            } catch (e: Exception) {
                _warmupGames.value = emptyList()
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WarmupsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WarmupsViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
