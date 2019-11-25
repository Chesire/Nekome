package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.flags.UserSeriesStatus

/**
 * Converter for [UserSeriesStatus] -> [String].
 *
 * For saving an [UserSeriesStatus] into the database.
 */
class UserSeriesStatusConverter {
    /**
     * Converts a [UserSeriesStatus] into a [String].
     */
    @TypeConverter
    fun fromUserSeriesStatus(status: UserSeriesStatus): String = status.name

    /**
     * Converts a [String] into a [StUserSeriesStatusring].
     */
    @TypeConverter
    fun toUserSeriesStatus(status: String): UserSeriesStatus = UserSeriesStatus.valueOf(status)
}
