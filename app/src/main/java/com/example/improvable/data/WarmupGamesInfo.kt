package com.example.improvable.data
import kotlinx.serialization.InternalSerializationApi

import kotlinx.serialization.Serializable

// a Warmup Game is very similar to a game
//
@OptIn(InternalSerializationApi::class)
@Serializable
data class WarmupGamesInfo (
    val id: String,
    val title: String,
    val minPlayers: Int,
    val maxPlayers: Int?, // some games don't have cap
    val description: String,
    val lastPlayed: Long? = null, // timestamp
    val tags: List<String> = emptyList() // SO WE CAN USE A SEARCH FUNCTION
)