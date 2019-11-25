package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.flags.SeriesType

/**
 * Converter for [SeriesType] -> [String].
 *
 * For saving an [SeriesType] into the database.
 */
class SeriesTypeConverter {
    /**
     * Converts a [SeriesType] into a [String].
     */
    @TypeConverter
    fun fromSeriesType(type: SeriesType): String = type.name

    /**
     * Converts a [String] into a [SeriesType].
     */
    @TypeConverter
    fun toSeriesType(type: String): SeriesType = SeriesType.valueOf(type)
}
