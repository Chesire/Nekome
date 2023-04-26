package com.chesire.nekome.core.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinkModel(
    val id: String,
    val displayText: String,
    val linkText: String
)
