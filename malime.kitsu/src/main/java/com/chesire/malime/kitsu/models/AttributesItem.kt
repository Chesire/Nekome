package com.chesire.malime.kitsu.models

data class AttributesItem(
    val slug: String,
    val canonicalTitle: String,
    val status: String,
    val progress: Int,
    val subtype: String,
    val posterImage: Map<String, Any>?,
    val coverImage: Map<String, Any>?,
    val episodeCount: Int,
    val chapterCount: Int,
    val nsfw: Boolean,
    val startedAt: String?,
    val finishedAt: String?
)