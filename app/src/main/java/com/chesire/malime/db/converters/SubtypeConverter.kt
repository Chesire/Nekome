package com.chesire.malime.db.converters

import androidx.room.TypeConverter
import com.chesire.malime.server.flags.Subtype

class SubtypeConverter {
    @TypeConverter
    fun fromSubtype(type: Subtype): String = type.name

    @TypeConverter
    fun toSubtype(type: String): Subtype = Subtype.valueOf(type)
}
