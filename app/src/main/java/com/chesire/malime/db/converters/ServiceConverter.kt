package com.chesire.malime.db.converters

import androidx.room.TypeConverter
import com.chesire.malime.server.flags.Service

class ServiceConverter {
    @TypeConverter
    fun fromService(service: Service): String = service.name

    @TypeConverter
    fun toService(service: String): Service = Service.valueOf(service)
}
