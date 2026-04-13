package com.example.improvable.data

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable

// Data for a single scene.
data class SceneInfo (
    var game: GamesInfo? = null,
    val date: Long, // timestamp
    val players: List<RosterInfo> = listOf(),
    var notes: String,
    var thumbnailPath: String, //path to image file for this scene.
    var recording: String, //path to mp3 file for this scene.
)