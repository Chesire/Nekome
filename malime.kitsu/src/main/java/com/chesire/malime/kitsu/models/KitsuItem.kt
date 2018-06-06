package com.chesire.malime.kitsu.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class KitsuItem(
    @PrimaryKey
    val seriesId: Int,
    val userSeriesId: Int,
    val type: String,
    val slug: String,
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