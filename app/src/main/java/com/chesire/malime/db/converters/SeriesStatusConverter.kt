package com.chesire.malime.db.converters

import androidx.room.TypeConverter
import com.chesire.malime.server.flags.SeriesStatus

class SeriesStatusConverter {
    @TypeConverter
    fun fromSeriesStatus(status: SeriesStatus): String = status.name

    @TypeConverter
    fun toSeriesStatus(status: String): SeriesStatus = SeriesStatus.valueOf(status)
}
