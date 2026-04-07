package com.example.improvable.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.improvable.data.GamesInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

// Warmups View Model --> 4/7 implementation, very similar to RosterViewModel and GamesViewModel w/o bells and whistles
class WarmupsViewModel(private val context: Context) : ViewModel() {
    private val _warmupGames = MutableStateFlow<List<GamesInfo>>(emptyList())
    val warmupGames: StateFlow<List<GamesInfo>> = _warmupGames.asStateFlow()

    init {
        loadWarmupGames() // load in games
    }

    // FUTURE:
    // implementation where it can suggest a random warmup game as a sort of popup

    private fun loadWarmupGames() {
        viewModelScope.launch {
            try {
                val jsonString = context.assets.open("warmupGames.json").bufferedReader().use { it.readText() }
                _warmupGames.value = Json.decodeFromString<List<GamesInfo>>(jsonString)
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
