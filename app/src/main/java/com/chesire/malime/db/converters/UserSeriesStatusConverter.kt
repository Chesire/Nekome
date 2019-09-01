package com.chesire.malime.db.converters

import androidx.room.TypeConverter
import com.chesire.malime.server.flags.UserSeriesStatus

class UserSeriesStatusConverter {
    @TypeConverter
    fun fromUserSeriesStatus(status: UserSeriesStatus): String = status.name

    @TypeConverter
    fun toUserSeriesStatus(status: String): UserSeriesStatus = UserSeriesStatus.valueOf(status)
}
