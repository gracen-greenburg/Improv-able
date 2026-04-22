package com.example.improvable.data

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppPreferences {
    companion object {
        private const val PREFS_PATH = "improvableSharedPrefs"
        private const val SUGGESTIONS_QUESTIONS_KEY = "questions"
        private const val SUGGESTIONS_GENERAL_KEY = "general suggestions"
        private const val SUGGESTIONS_JSON_PATH = "suggestionLists.json"

        private const val GAMES_JSON_PATH = "gamesInfo.json"


        private fun prefs(context: Context) =
            context.getSharedPreferences(PREFS_PATH, Context.MODE_PRIVATE)

        fun loadSuggestionsJsonStringFromPrefs(context: Context) : String {
            val p = context.getSharedPreferences(PREFS_PATH, Context.MODE_PRIVATE)
            return p.getString(SUGGESTIONS_JSON_PATH, "")!!
        }

        fun saveSuggestionsToPrefs(context: Context, questions: List<String>, generalSuggestions: ArrayList<String>) {
            val allSuggestions = ArrayList<SuggestionsList>()
            allSuggestions.add(SuggestionsList(SUGGESTIONS_QUESTIONS_KEY, questions))
            allSuggestions.add(SuggestionsList(SUGGESTIONS_GENERAL_KEY, generalSuggestions))
            val jsonString = Json.encodeToString(allSuggestions)
            prefs(context).edit { putString(SUGGESTIONS_JSON_PATH, jsonString) }
        }

        fun saveGamesToPrefs(context: Context, games: List<GamesInfo>) {
            val jsonString = Json.encodeToString(games)
            prefs(context).edit { putString(GAMES_JSON_PATH, jsonString) }
        }

        fun loadGamesFromPrefs(context: Context) : List<GamesInfo> {
            val p = context.getSharedPreferences(PREFS_PATH, Context.MODE_PRIVATE)
            val jsonString = p.getString(GAMES_JSON_PATH, "")!!
            return Json.decodeFromString<List<GamesInfo>>(jsonString)
        }

        fun loadSessionsFromPrefs(context: Context) {

        }

//        fun saveSessionsToPrefs(context: Context, sessions: MutableStateFlow<ArrayList<SessionInfo>>) {
//            val jsonString = Json.encodeToString(sessions)
//
//            Log.d("D", "JSONSTRING: " +jsonString)
//        }
    }
}