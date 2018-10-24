package com.chesire.malime.core.room

import android.arch.persistence.room.TypeConverter
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus

class Converters {
    @TypeConverter
    fun fromItemType(type: ItemType) = type.internalId

    @TypeConverter
    fun toItemType(id: Int) = ItemType.getTypeForInternalId(id)

    @TypeConverter
    fun fromSubtype(subtype: Subtype) = subtype.internalId

    @TypeConverter
    fun toSubtype(id: Int) = Subtype.getSubtypeForInternalId(id)

    @TypeConverter
    fun fromSeriesStatus(status: SeriesStatus) = status.internalId

    @TypeConverter
    fun toSeriesStatus(id: Int) = SeriesStatus.getStatusForInternalId(id)

    @TypeConverter
    fun fromUserSeriesStatus(status: UserSeriesStatus) = status.internalId

    @TypeConverter
    fun toUserSeriesStatus(id: Int) = UserSeriesStatus.getStatusForInternalId(id)
}
