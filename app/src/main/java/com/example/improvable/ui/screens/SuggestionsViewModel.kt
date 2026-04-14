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

class SuggestionsViewModel(private val context: Context) : ViewModel() {
    private var _questions = MutableStateFlow<List<String>>(listOf<String>())
    private var _generalSuggestions = MutableStateFlow<ArrayList<String>>(arrayListOf<String>())

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
                val jsonString = context.assets.open("suggestionLists.json").bufferedReader().use { it.readText() }
                val allSuggestions = Json.decodeFromString<ArrayList<SuggestionsList>>(jsonString)
                var i = 0;
                while (i < allSuggestions.size) {
                    if (allSuggestions[i].category.equals("questions")) {
                        _questions = MutableStateFlow(allSuggestions[i].content)
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
        return _questions.value.random()
    }

    fun getSuggestion() : String {
        return _generalSuggestions.value.random()
    }

    fun addSuggestion(suggestion: String) {
        if (!_generalSuggestions.value.contains(suggestion)) {
            _generalSuggestions.value.add(suggestion)
            saveSuggestions()
        }
    }

    fun saveSuggestions() {
        val allSuggestions = ArrayList<SuggestionsList>()
        allSuggestions.add(SuggestionsList("questions", _questions.value))
        allSuggestions.add(SuggestionsList("general suggestions", _generalSuggestions.value))
        val jsonString = Json.encodeToString(allSuggestions)
//        Log.d("D", "FIRST: " + jsonString)
        context.openFileOutput("suggestionLists.json", Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
//        Log.d("D", "SECOND: " + context.assets.open("suggestionLists.json").bufferedReader().use { it.readText() })
    }
}