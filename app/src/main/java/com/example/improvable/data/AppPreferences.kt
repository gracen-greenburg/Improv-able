package com.example.improvable.data

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class AppPreferences {
    companion object {
        private const val PREFS_PATH = "improvableSharedPrefs"
        private const val SUGGESTIONS_QUESTIONS_KEY = "questions"
        private const val SUGGESTIONS_GENERAL_KEY = "general suggestions"
        private const val SUGGESTIONS_JSON_PATH = "suggestionLists.json"

        private const val GAMES_JSON_PATH = "gamesInfo.json"
        private const val ROSTER_JSON_PATH = "rosterInfo.json"


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

        fun saveRosterToPrefs(context: Context, roster: List<RosterInfo>) {
            val jsonString = Json.encodeToString(roster)
            prefs(context).edit { putString(ROSTER_JSON_PATH, jsonString) }
        }

        fun loadRosterFromPrefs(context: Context) : List<RosterInfo> {
            val p = context.getSharedPreferences(PREFS_PATH, Context.MODE_PRIVATE)
            val jsonString = p.getString(ROSTER_JSON_PATH, "")!!
            return Json.decodeFromString<List<RosterInfo>>(jsonString)
        }

        // Reset all data to the default values in the source json files.
        fun resetAllData(context: Context) {
            // reset games info
            val gamesJsonString = context.assets.open(GAMES_JSON_PATH).bufferedReader().use { it.readText() }
            val games = Json.decodeFromString<List<GamesInfo>>(gamesJsonString)
            saveGamesToPrefs(context, games)

            // reset roster info
            val rosterJsonString = context.assets.open(ROSTER_JSON_PATH).bufferedReader().use { it.readText() }
            val players = Json.decodeFromString<List<RosterInfo>>(rosterJsonString)
            saveRosterToPrefs(context, players)

            // reset suggestions info
            val suggestJsonString = context.assets.open(SUGGESTIONS_JSON_PATH).bufferedReader().use { it.readText() }
            val suggest = Json.decodeFromString<List<SuggestionsList>>(rosterJsonString)
            // these are currently hardcoded. fix if more suggestion categories are ever implemented.
            val q = if (suggest[0].category == SUGGESTIONS_QUESTIONS_KEY) suggest[0].content else suggest[1].content
            var g = if (suggest[0].category == SUGGESTIONS_GENERAL_KEY) suggest[0].content else suggest[1].content
            g = ArrayList(g)
            saveSuggestionsToPrefs(context, q, g)
        }

//        fun saveSessionsToPrefs(context: Context, sessions: MutableStateFlow<ArrayList<SessionInfo>>) {
//            val jsonString = Json.encodeToString(sessions)
//
//            Log.d("D", "JSONSTRING: " +jsonString)
//        }
    }
}