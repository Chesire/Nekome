package com.chesire.nekome.kitsu.adapters

import com.chesire.nekome.core.flags.RatingSystem
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val ADVANCED = "advanced"
private const val REGULAR = "regular"
private const val SIMPLE = "simple"
private const val UNKNOWN = "unknown"

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
    fun ratingToString(ratingSystem: RatingSystem): String {
        return when (ratingSystem) {
            RatingSystem.Advanced -> ADVANCED
            RatingSystem.Regular -> REGULAR
            RatingSystem.Simple -> SIMPLE
            else -> UNKNOWN
        }
    }
}
