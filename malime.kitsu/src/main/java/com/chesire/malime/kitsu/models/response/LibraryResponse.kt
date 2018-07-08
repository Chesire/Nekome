package com.chesire.malime.kitsu.models.response

import com.chesire.malime.kitsu.models.LibraryItem
import com.google.gson.annotations.SerializedName

data class LibraryResponse(
    @SerializedName("data")
    val data: Array<LibraryItem>,
    @SerializedName("included")
    val included: Array<LibraryItem>,
    @SerializedName("meta")
    val meta: Map<String, Int>,
    @SerializedName("links")
    val links: Map<String, String>
)