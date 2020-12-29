package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.dataflags.Subtype

/**
 * Converter for [Subtype] -> [String].
 *
 * For saving an [Subtype] into the database.
 */
class SubtypeConverter {
    /**
     * Converts a [Subtype] into a [String].
     */
    @TypeConverter
    fun fromSubtype(type: Subtype): String = type.name

    /**
     * Converts a [String] into a [Subtype].
     */
    @TypeConverter
    fun toSubtype(type: String): Subtype = Subtype.valueOf(type)
}
