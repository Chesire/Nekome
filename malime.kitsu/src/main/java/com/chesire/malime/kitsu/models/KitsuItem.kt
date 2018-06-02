package com.chesire.malime.kitsu.models

data class KitsuItem(
    val seriesId: Int,
    val userSeriesId: Int,
    val type: String,
    private val slug: String,
    val canonicalTitle: String,
    val seriesStatus: String,
    val userSeriesStatus: String,
    val progress: Int,
    //val posterImageSizes
    //val coverImageSizes
    val episodeCount: Int,
    val chapterCount: Int,
    val nsfw: Boolean
)