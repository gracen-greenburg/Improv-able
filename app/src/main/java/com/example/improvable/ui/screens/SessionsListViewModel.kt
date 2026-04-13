package com.example.improvable.ui.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.improvable.data.SceneInfo
import com.example.improvable.data.SessionInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class SessionsListViewModel(private val context: Context) : ViewModel() {
    private val _allSessions = MutableStateFlow<ArrayList<SessionInfo>>(arrayListOf<SessionInfo>())
    var currentSession : SessionInfo = SessionInfo(emptyList(), 0, "Null session. This should never appear for the user.")
    var currentScene : SceneInfo = SceneInfo(null, 0, emptyList(), "Null scene. This should never appear for the user.", "", "")

    init {
        loadSessions() // load games
    }

    // Copied from GamesViewModel. May be overcomplicating things, but I wanted it to show all of the sessions right now with the option to add search or filter.
    val displayedSessions: StateFlow<List<SessionInfo>> = _allSessions
        .combine(_allSessions) { text, sessions ->
            sessions
        }
        .stateIn(
            scope = viewModelScope, // start state flow
            started = SharingStarted.WhileSubscribed(5000), // keeping it alive 5 seconds after being fiddled with
            initialValue = emptyList()
        )

    private fun loadSessions() {
        viewModelScope.launch {
            try {
                val jsonString = context.assets.open("sessionInfo.json").bufferedReader().use { it.readText() }
                _allSessions.value = Json{ignoreUnknownKeys=true}.decodeFromString<ArrayList<SessionInfo>>(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // utilized https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories as a ref
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SessionsListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SessionsListViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun setCurSesh(sessionInfo: SessionInfo) {
        if (!_allSessions.value.contains(sessionInfo)) {
            _allSessions.value.add(sessionInfo)
        }
        currentSession = sessionInfo
        Log.d("D", "SET CURRENT SESH: " +currentSession)
    }

    fun getCurSesh() : SessionInfo {
        return currentSession
    }
}