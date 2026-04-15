package com.example.improvable.data

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
// Raw data for a session for reading the JSON and translating to a SessionInfo
data class RawSessionInfo (
    val scenes : List<RawSceneInfo>,
    val date: Long, // timestamp
    var notes: String,
)