package com.example.improvable.data

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable

// Data for a single scene.
data class SceneInfo (
    val game: GamesInfo?,
    val date: Long, // timestamp
    val players: List<RosterInfo>?,
    val notes: String,
    val thumbnailPath: String, //path to image file for this scene.
    val recording: String, //path to mp3 file for this scene.
)