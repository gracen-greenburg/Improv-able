package com.example.improvable.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.improvable.data.GamesInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GamesViewModel(private val context: Context) : ViewModel() {
    private val _allGames = MutableStateFlow<List<GamesInfo>>(emptyList()) // games can change over time
    
    private val _searchText = MutableStateFlow("") // search can change on the inside
    val searchText = _searchText.asStateFlow() // but on the outside it is read only

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

    init {
        loadGames() // load games
    }

    private fun loadGames() {
        // show off all our data
        viewModelScope.launch {
            try {
                val jsonString = context.assets.open("gamesInfo.json").bufferedReader().use { it.readText() }
                _allGames.value = Json.decodeFromString<List<GamesInfo>>(jsonString)
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
            if (modelClass.isAssignableFrom(GamesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GamesViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
