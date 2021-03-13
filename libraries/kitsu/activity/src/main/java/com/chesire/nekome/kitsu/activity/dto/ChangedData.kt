package com.chesire.nekome.kitsu.activity.dto

/**
 * Container for the [ChangedData] events from the API.
 */
data class ChangedDataContainer(
    val changedData: List<ChangedData>
)

data class ChangedData(
    val type: String,
    val from: String,
    val to: String
)
