package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.flags.Subtype

class SubtypeConverter {
    @TypeConverter
    fun fromSubtype(type: Subtype): String = type.name

    @TypeConverter
    fun toSubtype(type: String): Subtype = Subtype.valueOf(type)
}
