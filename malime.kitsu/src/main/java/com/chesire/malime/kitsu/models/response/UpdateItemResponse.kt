package com.chesire.malime.kitsu.models.response

import com.chesire.malime.kitsu.models.LibraryItem
import com.google.gson.annotations.SerializedName

data class UpdateItemResponse(
    @SerializedName("data")
    val data: LibraryItem
)