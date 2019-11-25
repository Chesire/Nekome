package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.flags.SeriesStatus

/**
 * Converter for [SeriesStatus] -> [String].
 *
 * For saving an [SeriesStatus] into the database.
 */
class SeriesStatusConverter {
    /**
     * Converts a [SeriesStatus] into a [String].
     */
    @TypeConverter
    fun fromSeriesStatus(status: SeriesStatus): String = status.name

    /**
     * Converts a [String] into a [SeriesStatus].
     */
    @TypeConverter
    fun toSeriesStatus(status: String): SeriesStatus = SeriesStatus.valueOf(status)
}
