package com.chesire.nekome.kitsu.user.adapter

import com.chesire.nekome.seriesflags.RatingSystem
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val ADVANCED = "advanced"
private const val REGULAR = "regular"
private const val SIMPLE = "simple"
private const val UNKNOWN = "unknown"

/**
 * Adapter to plug into retrofit to convert strings into [RatingSystem] values.
 */
class RatingSystemAdapter {
    /**
     * Converts [ratingString] into the [RatingSystem] value.
     */
    @FromJson
    fun ratingFromString(ratingString: String): RatingSystem {
        return when (ratingString) {
            ADVANCED -> RatingSystem.Advanced
            REGULAR -> RatingSystem.Regular
            SIMPLE -> RatingSystem.Simple
            else -> RatingSystem.Unknown
        }
    }

    /**
     * Converts [ratingSystem] into its [String] value.
     */
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
