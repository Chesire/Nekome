package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.flags.Service

/**
 * Converter for [Service] -> [String].
 *
 * For saving an [Service] into the database.
 */
class ServiceConverter {
    /**
     * Converts a [Service] into a [String].
     */
    @TypeConverter
    fun fromService(service: Service): String = service.name

    /**
     * Converts a [String] into a [Service].
     */
    @TypeConverter
    fun toService(service: String): Service = Service.valueOf(service)
}
