package com.chesire.nekome.kitsu.activity.dto

sealed class ChangedData {
    data class Rating(
        val from: Int,
        val to: Int
    ) : ChangedData()

    data class Progress(
        val from: Int,
        val to: Int
    ) : ChangedData()

    data class Status(
        val from: String,
        val to: String
    ) : ChangedData()
}
