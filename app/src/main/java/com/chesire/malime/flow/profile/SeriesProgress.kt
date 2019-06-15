package com.chesire.malime.flow.profile

data class SeriesProgress(
    val total: Int,
    val current: Int,
    val completed: Int,
    val onHold: Int,
    val dropped: Int,
    val planned: Int,
    val unknown: Int
)
