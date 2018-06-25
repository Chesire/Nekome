package com.chesire.malime.core.room

import android.arch.persistence.room.TypeConverter
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus

class Converters {
    @TypeConverter
    fun fromItemType(type: ItemType): Int {
        return type.internalId
    }

    @TypeConverter
    fun toItemType(id: Int): ItemType {
        return ItemType.getTypeForInternalId(id)
    }

    @TypeConverter
    fun fromSubtype(subtype: Subtype): Int {
        return subtype.internalId
    }

    @TypeConverter
    fun fromSubtype(id: Int): Subtype {
        return Subtype.getSubtypeForInternalId(id)
    }

    @TypeConverter
    fun fromSeriesStatus(status: SeriesStatus): Int {
        return status.internalId
    }

    @TypeConverter
    fun toSeriesStatus(id: Int): SeriesStatus {
        return SeriesStatus.getStatusForInternalId(id)
    }

    @TypeConverter
    fun fromUserSeriesStatus(status: UserSeriesStatus): Int {
        return status.internalId
    }

    @TypeConverter
    fun toUserSeriesStatus(id: Int): UserSeriesStatus {
        return UserSeriesStatus.getStatusForInternalId(id)
    }
}