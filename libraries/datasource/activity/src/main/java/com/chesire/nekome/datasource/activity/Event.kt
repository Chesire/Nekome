package com.chesire.nekome.datasource.activity

/**
 * Represents a single activity event.
 * [from] represents the value before the activity event.
 * [to] represents the value after the activity event.
 * [eventType] represents the type of event that has occurred.
 */
data class Event(
    val from: String,
    val to: String,
    val eventType: String
)
