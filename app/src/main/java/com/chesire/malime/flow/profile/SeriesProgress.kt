package com.chesire.malime.flow.profile

data class SeriesProgress(
    val total: String,
    val current: String,
    val completed: String,
    val onHold: String,
    val dropped: String,
    val planned: String,
    val unknown: String
)
