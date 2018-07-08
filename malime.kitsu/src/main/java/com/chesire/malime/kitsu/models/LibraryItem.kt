package com.chesire.malime.kitsu.models

import com.google.gson.annotations.SerializedName

data class LibraryItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("links")
    val links: Map<String, String>,
    @SerializedName("attributes")
    val attributes: AttributesItem
)