package com.example.improvable.data


// the basis of our roster
data class RosterInfo (
    val firstName: String,
    val lastName: String,
    val returning: Boolean, // have they been here before?
    val year: Int, // what year are they (1 freshman -> 4 Senior -> 0 other
    //val gamesPlayed: List<String> = emptyList(), // list of games plus practice date --> LATER
    // --> maybe this should be attached to the game itself
    val coreCast: Boolean, // are they on core cast
    val attendance: List<Boolean> = emptyList(), // List of whether they attended practice or not
            // with attendance, we can make it a percentage, see how frequent people are attending practice
    val notes: List<String> = emptyList(), // leader can give notes
    val president: Boolean // if president, then can give notes and edit roster
)