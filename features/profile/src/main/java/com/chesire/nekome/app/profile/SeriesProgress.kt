package com.chesire.nekome.app.profile

/**
 * Container for the different states a series can be in, aids with displaying on the profile.
 */
data class SeriesProgress(
    val total: String,
    val current: String,
    val completed: String,
    val onHold: String,
    val dropped: String,
    val planned: String,
    val unknown: String
)
