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
    private val _allGames = MutableStateFlow<List<GamesInfo>>(emptyList())
    
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val filteredGames: StateFlow<List<GamesInfo>> = _searchText
        .combine(_allGames) { text, games ->
            if (text.isBlank()) {
                games
            } else {
                val searchInt = text.toIntOrNull()
                games.filter { game ->
                    val matchesText = game.title.contains(text, ignoreCase = true) ||
                                     game.tags.any { tag -> tag.contains(text, ignoreCase = true) }
                    
                    val matchesPlayerCount = if (searchInt != null) {
                        searchInt >= game.minPlayers && (game.maxPlayers == null || searchInt <= game.maxPlayers)
                    } else false

                    matchesText || matchesPlayerCount
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadGames()
    }

    private fun loadGames() {
        viewModelScope.launch {
            try {
                val jsonString = context.assets.open("gamesInfo.json").bufferedReader().use { it.readText() }
                _allGames.value = Json.decodeFromString<List<GamesInfo>>(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

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
