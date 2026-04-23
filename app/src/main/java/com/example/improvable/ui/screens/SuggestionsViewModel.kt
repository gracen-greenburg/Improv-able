package com.example.improvable.ui.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.improvable.data.SceneInfo
import com.example.improvable.data.SessionInfo
import com.example.improvable.data.SuggestionsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.OutputStream
import androidx.core.content.edit
import com.example.improvable.data.AppPreferences

class SuggestionsViewModel(private val context: Context) : ViewModel() {
    private var _questions = MutableStateFlow<ArrayList<String>>(arrayListOf<String>())
    private var _generalSuggestions = MutableStateFlow<ArrayList<String>>(arrayListOf<String>())

    private var availableQuestions = _questions.value
    private var availableSuggestions = _generalSuggestions.value

    init {
        loadSessions() // load games
    }

    // utilized https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories as a ref
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SuggestionsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SuggestionsViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun loadSessions() {
        viewModelScope.launch {
            try {
                val jsonString = AppPreferences.loadSuggestionsJsonStringFromPrefs(context)
                val allSuggestions = Json.decodeFromString<ArrayList<SuggestionsList>>(jsonString)
                var i = 0;
                while (i < allSuggestions.size) {
                    if (allSuggestions[i].category.equals("questions")) {
                        _questions = MutableStateFlow(ArrayList(allSuggestions[i].content))
                    } else if (allSuggestions[i].category.equals("general suggestions")) {
                        _generalSuggestions = MutableStateFlow(ArrayList<String>(allSuggestions[i].content))
                    }
                    i++;
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getQuestion() : String {
        if (availableQuestions.isEmpty()) availableQuestions = ArrayList(_questions.value)
        val picked = availableQuestions.random()
        availableQuestions.remove(picked)
        return picked
    }

    fun getSuggestion() : String {
        if (availableSuggestions.isEmpty()) availableSuggestions = ArrayList(_generalSuggestions.value)
        val picked = availableSuggestions.random()
        availableSuggestions.remove(picked)
        return picked
    }

    fun addSuggestion(suggestion: String) {
        if (!_generalSuggestions.value.contains(suggestion)) {
            _generalSuggestions.value.add(suggestion)
            AppPreferences.saveSuggestionsToPrefs(context, _questions.value, _generalSuggestions.value)
        }
    }
}