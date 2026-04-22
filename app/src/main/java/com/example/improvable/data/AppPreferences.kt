package com.example.improvable.data

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.example.improvable.data.RosterInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class AppPreferences {
    companion object {
        private const val PREFS_PATH = "improvableSharedPrefs"
        private const val IS_INITIALIZED = "initialized?"
        private const val SUGGESTIONS_QUESTIONS_KEY = "questions"
        private const val SUGGESTIONS_GENERAL_KEY = "general suggestions"
        private const val SUGGESTIONS_JSON_PATH = "suggestionLists.json"
        private const val GAMES_JSON_PATH = "gamesInfo.json"
        private const val ROSTER_JSON_PATH = "rosterInfo.json"
        private const val SESSIONS_JSON_PATH = "sessionInfo.json"

        private fun prefs(context: Context) =
            context.getSharedPreferences(PREFS_PATH, Context.MODE_PRIVATE)

        // Check to see if app preferences have been set up on this device.
        // If not, add all default app preferences.
        fun checkInit(context: Context) {
            if (!prefs(context).contains(IS_INITIALIZED)) {
                resetAllData(context)
                prefs(context).edit { putBoolean(IS_INITIALIZED, true) }
            }
        }

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

        fun saveSessionsToPrefs(context: Context, sessions: List<SessionInfo>) {
            //convert real session data to raw session data
            var rawSeshList = arrayListOf<RawSessionInfo>()
            for (sesh in sessions) {
                var rawScenes = arrayListOf<RawSceneInfo>()
                for (scene in sesh.scenes) {

                    var ps = ArrayList<String>()
                    for (p in scene.players) {
                        ps.add(p.id)
                    }
                    var rsi = RawSceneInfo(
                        scene.game!!.id,
                        scene.date,
                        ps,
                        scene.notes,
                        scene.thumbnailPath,
                        scene.recording
                    )
                    rawScenes.add(rsi)
                }
                rawSeshList.add(
                    RawSessionInfo(
                        rawScenes,
                        sesh.date,
                        sesh.notes
                    )
                )
            }
            saveRawSessionsToPrefs(context, rawSeshList)
        }

        fun saveRawSessionsToPrefs(context: Context, rawSessions: List<RawSessionInfo>) {
            val jsonString = Json.encodeToString(rawSessions)
            prefs(context).edit { putString(SESSIONS_JSON_PATH, jsonString) }
        }

        fun loadSessionsFromPrefs(context: Context) : List<SessionInfo> {
            var seshList = arrayListOf<SessionInfo>()

            // load in games and players data
            val games = loadGamesFromPrefs(context)
            val roster = loadRosterFromPrefs(context)

            // load in raw session data
            val p = context.getSharedPreferences(PREFS_PATH, Context.MODE_PRIVATE)
            val jsonString = p.getString(SESSIONS_JSON_PATH, "")!!
            val rawSessions = Json.decodeFromString<ArrayList<RawSessionInfo>>(jsonString)
            // convert raw session data to real session data
            for (rawSesh in rawSessions) {
                var scenes = ArrayList<SceneInfo>()
                for (rawScene in rawSesh.scenes) {
                    var gm: GamesInfo = GamesInfo("", "None", 0, 0, "")
                    for (g in games) {
                        if (rawScene.gameID == g.id) {
                            gm = g
                            break
                        }
                    }
                    var ps = ArrayList<RosterInfo>()
                    for (p in roster) {
                        if (rawScene.playerIDs.contains(p.id)) {
                            ps.add(p)
                        }
                    }
                    scenes.add(
                        SceneInfo(
                            gm,
                            rawScene.date,
                            ps,
                            rawScene.notes,
                            rawScene.thumbnailPath,
                            rawScene.recording
                        )
                    )
                }
                var sesh = SessionInfo(
                    scenes,
                    rawSesh.date,
                    rawSesh.notes
                )
                seshList.add(sesh)
            }
            return seshList
        }

        // Reset all data to the default values in the source json files.
        fun resetAllData(context: Context) {
            // reset games info
            val gamesJsonString = context.assets.open(GAMES_JSON_PATH).bufferedReader().use { it.readText() }
            val games = Json{ignoreUnknownKeys = true}.decodeFromString<List<GamesInfo>>(gamesJsonString)
            saveGamesToPrefs(context, games)

            // reset roster info
            val rosterJsonString = context.assets.open(ROSTER_JSON_PATH).bufferedReader().use { it.readText() }
            val players = Json{ignoreUnknownKeys = true}.decodeFromString<List<RosterInfo>>(rosterJsonString)
            saveRosterToPrefs(context, players)

            // reset suggestions info
            val suggestJsonString = context.assets.open(SUGGESTIONS_JSON_PATH).bufferedReader().use { it.readText() }
            val suggest = Json{ignoreUnknownKeys = true}.decodeFromString<List<SuggestionsList>>(suggestJsonString)
            // these are currently hardcoded. fix if more suggestion categories are ever implemented.
            val q = if (suggest[0].category == SUGGESTIONS_QUESTIONS_KEY) suggest[0].content else suggest[1].content
            var g = if (suggest[0].category == SUGGESTIONS_GENERAL_KEY) suggest[0].content else suggest[1].content
            g = ArrayList(g)
            saveSuggestionsToPrefs(context, q, g)

            // reset sessions info
            val seshJsonString = context.assets.open("sessionInfo.json").bufferedReader().use { it.readText() }
            val sessions = Json{ignoreUnknownKeys = true}.decodeFromString<List<RawSessionInfo>>(seshJsonString)
            saveRawSessionsToPrefs(context, sessions)

            // reset warmups info

        }

//        fun saveSessionsToPrefs(context: Context, sessions: MutableStateFlow<ArrayList<SessionInfo>>) {
//            val jsonString = Json.encodeToString(sessions)
//
//            Log.d("D", "JSONSTRING: " +jsonString)
//        }
    }
}