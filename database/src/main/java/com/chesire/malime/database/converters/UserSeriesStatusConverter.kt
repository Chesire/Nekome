package com.chesire.malime.database.converters

import androidx.room.TypeConverter
import com.chesire.malime.core.flags.UserSeriesStatus

class UserSeriesStatusConverter {
    @TypeConverter
    fun fromUserSeriesStatus(status: UserSeriesStatus): String = status.name

    @TypeConverter
    fun toUserSeriesStatus(status: String): UserSeriesStatus = UserSeriesStatus.valueOf(status)
}
