package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.models.LinkModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Converter for list of [LinkModel] -> [String].
 *
 * For saving a list of [LinkModel] into the database.
 */
class LinkModelsListConverter {

    /**
     * Converts a list of [LinkModel] into a [String].
     */
    @TypeConverter
    fun fromListOfLinkModels(links: List<LinkModel>): String = Gson().toJson(links)

    /**
     * Converts a [String] into a list of [LinkModel].
     */
    @TypeConverter
    fun toListOfLinkModels(linksJson: String): List<LinkModel> =
        Gson().fromJson(linksJson, object : TypeToken<List<LinkModel?>?>() {}.type) ?: emptyList()
}
