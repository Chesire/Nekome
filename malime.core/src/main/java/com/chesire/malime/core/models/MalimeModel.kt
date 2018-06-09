package com.chesire.malime.core.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.UserSeriesStatus

@Entity
data class MalimeModel(
    var seriesId: Int,
    var userSeriesId: Int,
    var type: ItemType,
    var slug: String,
    var title: String,
    var seriesStatus: SeriesStatus,
    var userSeriesStatus: UserSeriesStatus,
    var progress: Int,
    var totalLength: Int,
    var posterImage: String,
    var coverImage: String,
    var nsfw: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    // id needs to be null otherwise autoGenerate will not work
    // and Room will use the id assigned to it
    var id: Int? = null

    fun getTotalSeriesLength(): String =
        if (totalLength == 0) {
            "??"
        } else {
            totalLength.toString()
        }
}