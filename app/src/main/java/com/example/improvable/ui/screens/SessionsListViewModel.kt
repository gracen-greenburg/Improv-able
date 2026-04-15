package com.example.improvable.ui.screens

import android.R
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.improvable.data.GamesInfo
import com.example.improvable.data.RawSessionInfo
import com.example.improvable.data.RosterInfo
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
                // init _allSessions
                _allSessions.value = arrayListOf<SessionInfo>()
                // load in games and players data
                val gamesJsonString = context.assets.open("gamesInfo.json").bufferedReader().use { it.readText() }
                val games = Json.decodeFromString<List<GamesInfo>>(gamesJsonString)
                val playersJsonString = context.assets.open("rosterInfo.json").bufferedReader().use { it.readText() }
                val players = Json.decodeFromString<List<RosterInfo>>(playersJsonString)
                // load in raw session data
                val jsonString = context.assets.open("sessionInfo.json").bufferedReader().use { it.readText() }
                val rawSessions = Json.decodeFromString<ArrayList<RawSessionInfo>>(jsonString)
                // convert raw session data to real session data
                for (rawSesh in rawSessions) {
                    var scenes = ArrayList<SceneInfo>()
                    for (rawScene in rawSesh.scenes) {
                        var gm : GamesInfo = GamesInfo("", "None", 0, 0, "")
                        for (g in games) {
                            if (rawScene.gameID == g.id) {
                                gm = g
                                break
                            }
                        }
                        var ps = ArrayList<RosterInfo>()
                        for (p in players) {
                            if (rawScene.playerIDs.contains(p.id)) {
                                ps.add(p)
                            }
                        }
                        scenes.add(SceneInfo(
                            gm,
                            rawScene.date,
                            ps,
                            rawScene.notes,
                            rawScene.thumbnailPath,
                            rawScene.recording
                        ))
                    }
                    var sesh = SessionInfo(
                        scenes,
                        rawSesh.date,
                        rawSesh.notes
                    )
                    _allSessions.value.add(sesh)
                }

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