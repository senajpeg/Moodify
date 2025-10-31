package com.senaaksoy.moodify.model

import com.google.firebase.Timestamp

data class MoodEntry(
    val mood: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val note: String? = null
)