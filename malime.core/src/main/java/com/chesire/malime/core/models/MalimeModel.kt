package com.chesire.malime.core.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.chesire.malime.core.ItemType

@Entity
data class MalimeModel(
    val seriesId: Int,
    val userSeriesId: Int,
    val type: ItemType,
    val slug: String,
    val title: String,
    val seriesStatus: String,
    val userSeriesStatus: String,
    val progress: Int,
    //val posterImageSizes
    //val coverImageSizes
    val totalLength: Int,
    val nsfw: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    // id needs to be null otherwise autoGenerate will not work
    // and Room will use the id assigned to it
    var id: Int? = null
}