package com.chesire.nekome.kitsu.activity.dto

/**
 * Container for the [ChangedData] events from the API.
 */
data class ChangedDataContainer(
    val changedData: List<ChangedData>
)

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
