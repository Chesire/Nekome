package com.chesire.malime.core.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.chesire.malime.core.ItemType

@Entity
data class MalimeModel(
    var seriesId: Int,
    var userSeriesId: Int,
    var type: ItemType,
    var slug: String,
    var title: String,
    var seriesStatus: String,
    var userSeriesStatus: String,
    var progress: Int,
    //var posterImageSizes
    //var coverImageSizes
    var totalLength: Int,
    var nsfw: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    // id needs to be null otherwise autoGenerate will not work
    // and Room will use the id assigned to it
    var id: Int? = null
}