package com.chesire.malime.kitsu.models

data class AttributesItem(
    val slug: String,
    val canonicalTitle: String,
    val status: String,
    val progress: Int,
    // val posterImage
    // val coverImage
    val episodeCount: Int,
    val chapterCount: Int,
    val nsfw: Boolean
)