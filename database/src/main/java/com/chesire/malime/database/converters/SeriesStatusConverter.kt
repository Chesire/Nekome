package com.chesire.malime.database.converters

import androidx.room.TypeConverter
import com.chesire.malime.core.flags.SeriesStatus

class SeriesStatusConverter {
    @TypeConverter
    fun fromSeriesStatus(status: SeriesStatus): String = status.name

    @TypeConverter
    fun toSeriesStatus(status: String): SeriesStatus = SeriesStatus.valueOf(status)
}
