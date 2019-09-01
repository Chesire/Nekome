package com.chesire.malime.db.converters

import androidx.room.TypeConverter
import com.chesire.malime.core.flags.SeriesType

class SeriesTypeConverter {
    @TypeConverter
    fun fromSeriesType(type: SeriesType): String = type.name

    @TypeConverter
    fun toSeriesType(type: String): SeriesType = SeriesType.valueOf(type)
}
