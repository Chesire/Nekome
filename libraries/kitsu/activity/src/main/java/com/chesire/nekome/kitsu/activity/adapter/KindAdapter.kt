package com.chesire.nekome.kitsu.activity.adapter

import com.chesire.nekome.kitsu.activity.dto.Kind
import com.squareup.moshi.FromJson

private const val PROGRESSED = "progressed"
private const val RATED = "rated"
private const val UPDATED = "updated"

class KindAdapter {

    @FromJson
    fun kindFromString(kind: String): Kind {
        return when (kind) {
            PROGRESSED -> Kind.Progressed
            RATED -> Kind.Rated
            UPDATED -> Kind.Updated
            else -> Kind.Unknown
        }
    }
}
