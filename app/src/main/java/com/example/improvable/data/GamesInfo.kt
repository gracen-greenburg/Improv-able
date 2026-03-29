package com.example.improvable.data
// 3/29
import kotlinx.serialization.InternalSerializationApi
import java.util.Date // I want it to track when a game was last played eventully

import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class) // nightmare solution, but it works
@Serializable
data class GamesInfo (
    val id: String,
    val title: String,
    val minPlayers: Int,
    val maxPlayers: Int?, // some games don't have cap
    val description: String,
    val lastPlayed: Long? = null, // timestamp
    val tags: List<String> = emptyList() // SO WE CAN USE A SEARCH FUNCTION
)