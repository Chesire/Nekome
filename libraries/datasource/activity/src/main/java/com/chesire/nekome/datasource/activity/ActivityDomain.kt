package com.chesire.nekome.datasource.activity

/**
 * Provides a domain for a single piece of activity that has occurred.
 */
data class ActivityDomain(
    val id: Int,
    val timestamp: String,
    val events: List<Event>
)
