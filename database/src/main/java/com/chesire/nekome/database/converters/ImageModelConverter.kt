package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.models.ImageModelJsonAdapter
import com.squareup.moshi.Moshi

/**
 * Converter for [ImageModel] -> [String].
 *
 * For saving an [ImageModel] into the database.
 */
class ImageModelConverter {
    private val adapter: ImageModelJsonAdapter by lazy {
        ImageModelJsonAdapter(Moshi.Builder().build())
    }

    /**
     * Converts an [ImageModel] into a [String].
     */
    @TypeConverter
    fun fromImageModel(model: ImageModel): String = adapter.toJson(model)

    /**
     * Converts a [String] into a [ImageModel].
     */
    @TypeConverter
    fun toImageModel(model: String): ImageModel = adapter.fromJson(model) ?: ImageModel.empty
}
