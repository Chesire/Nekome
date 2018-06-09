package com.chesire.malime.kitsu.models

data class AttributesItem(
    val slug: String,
    val canonicalTitle: String,
    val status: String,
    val progress: Int,
    val posterImage: Map<String, String>,
    val coverImage: Map<String, String>,
    val episodeCount: Int,
    val chapterCount: Int,
    val nsfw: Boolean
)