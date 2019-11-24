package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.flags.SeriesType

class SeriesTypeConverter {
    @TypeConverter
    fun fromSeriesType(type: SeriesType): String = type.name

    @TypeConverter
    fun toSeriesType(type: String): SeriesType = SeriesType.valueOf(type)
}
