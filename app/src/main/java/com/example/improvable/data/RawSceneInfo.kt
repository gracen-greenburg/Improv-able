package com.example.improvable.data

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@OptIn(InternalSerializationApi::class)
@Serializable
// Raw data for a single scene for reading the JSON file then translating to a SceneInfo.
data class RawSceneInfo (
    var gameID: String = "",
    val date: Long, // timestamp
    val playerIDs: List<String> = listOf(),
    var notes: String,
    var thumbnailPath: String, //path to image file for this scene.
    var recording: String, //path to mp3 file for this scene.
)