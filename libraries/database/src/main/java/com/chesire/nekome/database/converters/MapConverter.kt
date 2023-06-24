package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Converter for [Map] -> [String].
 *
 * For saving the otherTitles into the database.
 */
class MapConverter {

    private val adapter: JsonAdapter<Map<String, String>> by lazy {
        val type = Types.newParameterizedType(
            Map::class.java,
            String::class.java,
            String::class.java
        )
        Moshi.Builder().build().adapter(type)
    }

    @TypeConverter
    fun fromMap(map: Map<String, String>): String = adapter.toJson(map)

    @TypeConverter
    fun toMap(data: String): Map<String, String> = if (data.isNotEmpty()) {
        adapter.fromJson(data) ?: emptyMap()
    } else {
        emptyMap()
    }
}
