package com.example.improvable.ui.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.improvable.data.AppPreferences
import com.example.improvable.data.RosterInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File

// 4/8 update -- Unclear how we are going to implememnt roster. Meeting to clear things up
// This class is v similar to GamesViewModel, trying to read the json fiel
class RosterViewModel(private val context: Context) : ViewModel() {
    private val _roster = MutableStateFlow<List<RosterInfo>>(emptyList())
    val roster = _roster.asStateFlow()

    private val rosterFile = File(context.filesDir, "rosterInfo.json") /// so we can update it

    // won't read my roster file so I looked it up
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    init {
        loadRoster() //load our roster
    }
    private fun loadRoster() {
        _roster.value = AppPreferences.loadRosterFromPrefs(context)
    }

    // marks and updates attendance for each person
    // As of 4/8 no longer taking attendance

    fun saveRoster() {
        viewModelScope.launch {
            try {
                val jsonString = json.encodeToString(_roster.value)
                rosterFile.writeText(jsonString)
            } catch (e: Exception) {
            }
        }
    }


    //
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RosterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RosterViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
