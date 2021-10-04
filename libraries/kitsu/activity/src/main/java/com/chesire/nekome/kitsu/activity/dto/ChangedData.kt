package com.chesire.nekome.kitsu.activity.dto

/**
 * Container for the [ChangedData] events from the API.
 */
data class ChangedDataContainer(
    val changedData: List<ChangedData>
)

/**
 * Data for an event of "activity" for a user.
 */
data class ChangedData(
    val type: String,
    val from: String,
    val to: String
)
