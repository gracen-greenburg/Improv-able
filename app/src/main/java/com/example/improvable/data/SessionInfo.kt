package com.example.improvable.data

// Joe Hardy

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable

// Data for a session, which includes many scenes.
data class SessionInfo (
    val scenes : List<SceneInfo>,
    val date: Long, // timestamp
    val notes: String,
)