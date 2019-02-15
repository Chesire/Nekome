package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.RatingSystem
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val ADVANCED = "advanced"
private const val REGULAR = "regular"
private const val SIMPLE = "simple"

@Suppress("unused")
class RatingSystemAdapter {
    @FromJson
    fun ratingFromString(ratingString: String): RatingSystem {
        return when (ratingString) {
            ADVANCED -> RatingSystem.Advanced
            REGULAR -> RatingSystem.Regular
            SIMPLE -> RatingSystem.Simple
            else -> RatingSystem.Unknown
        }
    }

    @ToJson
    fun ratingToJson(ratingSystem: RatingSystem): String {
        return when (ratingSystem) {
            RatingSystem.Advanced -> ADVANCED
            RatingSystem.Regular -> REGULAR
            RatingSystem.Simple -> SIMPLE
            else -> "unknown"
        }
    }
}
