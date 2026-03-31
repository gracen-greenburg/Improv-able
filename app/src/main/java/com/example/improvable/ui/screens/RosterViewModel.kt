package com.example.improvable.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.improvable.data.RosterInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File


// This class is v similar to GamesViewModel, trying to read the json fiel
class RosterViewModel(private val context: Context) : ViewModel() {
    private val _roster = MutableStateFlow<List<RosterInfo>>(emptyList())
    val roster = _roster.asStateFlow()

    private val rosterFile = File(context.filesDir, "rosterInfo.json") /// so we can update it


    init {
        loadRoster() //load our roster
    }

    private fun loadRoster() {
        viewModelScope.launch {
            try {
                val jsonString = if (rosterFile.exists()) {
                    rosterFile.readText()
                } else {
                    context.assets.open("rosterInfo.json").bufferedReader().use { it.readText() }
                }
                _roster.value = Json.decodeFromString<List<RosterInfo>>(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // marks and updates attendance for each person
    fun markAttendance(index: Int, isPresent: Boolean) {
        val currentRoster = _roster.value.toMutableList()
        if (index in currentRoster.indices) {
            val person = currentRoster[index]
            val updatedAttendance = person.attendance.toMutableList()
            updatedAttendance.add(isPresent)
            currentRoster[index] = person.copy(attendance = updatedAttendance)
            _roster.value = currentRoster
        }
    }

        fun saveRoster() {
            viewModelScope.launch {
                try {
                    val jsonString = Json.encodeToString(_roster.value)
                    rosterFile.writeText(jsonString)
                } catch (e: Exception) {
                    e.printStackTrace()
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
