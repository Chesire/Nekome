package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.models.LinkModel
import com.chesire.nekome.core.models.LinkModelJsonAdapter
import com.squareup.moshi.Moshi
import java.io.IOException

private const val SEPARATOR = ",,SEP,,"

/**
 * Converter for list of [LinkModel] -> [String].
 *
 * For saving a list of [LinkModel] into the database.
 */
class LinkModelsListConverter {

    // I'm sorry future me.

    private val adapter: LinkModelJsonAdapter by lazy {
        LinkModelJsonAdapter(Moshi.Builder().build())
    }

    /**
     * Converts a list of [LinkModel] into a [String].
     */
    @TypeConverter
    fun fromListOfLinkModels(links: List<LinkModel>): String {
        return links.joinToString(separator = SEPARATOR) { adapter.toJson(it) }
    }

    /**
     * Converts a [String] into a list of [LinkModel].
     */
    @TypeConverter
    fun toListOfLinkModels(linksJson: String): List<LinkModel> {
        return if (linksJson.isBlank()) {
            emptyList()
        } else {
            linksJson
                .split(SEPARATOR)
                .mapNotNull {
                    try {
                        adapter.fromJson(it)
                    } catch (ex: IOException) {
                        null
                    }
                }
        }
    }
}
