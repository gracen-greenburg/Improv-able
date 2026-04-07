package com.example.improvable.data

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

// Goal:
// track date, attendee's, games, and notes for each pratice
// later will implement recordings // pictures
@OptIn(InternalSerializationApi::class)
@Serializable
data class PracticeData(
    val id: String,
    val date: Long, // timestamp
    val attendeeIds: List<String>,
    val gameIds: List<String>, // LATER: maybe add a plus button to games played, auto adds to our data json
    val notes: String = ""
)