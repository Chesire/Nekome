package com.chesire.malime.db.converters

import androidx.room.TypeConverter
import com.chesire.malime.server.models.ImageModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class ImageModelConverter {
    private val adapter: JsonAdapter<ImageModel> by lazy {
        Moshi.Builder()
            .build()
            .adapter<ImageModel>(
                Types.newParameterizedType(ImageModel::class.java)
            )
    }

    @TypeConverter
    fun fromImageModel(model: ImageModel): String = adapter.toJson(model)

    @TypeConverter
    fun toImageModel(model: String): ImageModel = adapter.fromJson(model) ?: ImageModel.empty
}
