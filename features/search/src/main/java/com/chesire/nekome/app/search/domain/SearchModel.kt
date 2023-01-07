package com.chesire.nekome.app.search.domain

import android.os.Parcelable
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import kotlinx.parcelize.Parcelize

/**
 * Model containing information to show for a searched item.
 */
@Parcelize
data class SearchModel(
    val id: Int,
    val type: SeriesType,
    val synopsis: String,
    val canonicalTitle: String,
    val subtype: Subtype,
    val posterImage: ImageModel
) : Parcelable
