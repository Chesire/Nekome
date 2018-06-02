package com.chesire.malime.kitsu.models

data class LibraryItem(
    val id: Int,
    val type: String,
    val links: Map<String, String>,
    val attributes: AttributesItem
    // private val relationships:
)