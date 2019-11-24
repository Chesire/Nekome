package com.chesire.nekome.database.converters

import androidx.room.TypeConverter
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.models.ImageModelJsonAdapter
import com.squareup.moshi.Moshi

class ImageModelConverter {
    private val adapter: ImageModelJsonAdapter by lazy {
        ImageModelJsonAdapter(Moshi.Builder().build())
    }

    @TypeConverter
    fun fromImageModel(model: ImageModel): String = adapter.toJson(model)

    @TypeConverter
    fun toImageModel(model: String): ImageModel = adapter.fromJson(model) ?: ImageModel.empty
}
