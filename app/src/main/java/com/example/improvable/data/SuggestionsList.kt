package com.example.improvable.data

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable

// Data for a single scene.
data class SuggestionsList (
    val category : String = "",
    val content : List<String> = emptyList<String>()
)