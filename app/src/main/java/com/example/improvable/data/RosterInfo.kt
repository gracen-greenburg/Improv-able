package com.example.improvable.data

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import java.util.UUID

@OptIn(InternalSerializationApi::class) // worked for games, it'll work for this
@Serializable
// the basis of our roster
data class RosterInfo (
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val returning: Boolean, // have they been here before?
    val year: Int, // what year are they (1 freshman -> 4 Senior -> 0 other
    val coreCast: Boolean, // are they on core cast
)