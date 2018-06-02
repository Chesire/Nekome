package com.chesire.malime.kitsu.models

data class FilterResponse(
    val data: Array<FilterData>
    //val links:
    //val meta
)

data class FilterData(
    // private val attributes:
    val id: Int,
    // private val links:
    // private val relationships:
    val type: String
)