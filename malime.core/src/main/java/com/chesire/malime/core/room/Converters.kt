package com.chesire.malime.core.room

import android.arch.persistence.room.TypeConverter
import com.chesire.malime.core.ItemStatus
import com.chesire.malime.core.ItemType

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
    fun fromItemStatus(status: ItemStatus): Int {
        return status.internalId
    }

    @TypeConverter
    fun toItemStatus(id: Int): ItemStatus {
        return ItemStatus.getStatusForInternalId(id)
    }
}