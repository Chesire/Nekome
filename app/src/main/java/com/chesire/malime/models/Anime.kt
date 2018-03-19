package com.chesire.malime.models

import android.os.Parcel
import android.os.Parcelable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "anime")
data class Anime(
    @field:Element(name = "series_animedb_id", required = false)
    @param:Element(name = "series_animedb_id", required = false)
    val seriesAnimeDbId: Int? = null,

    @field:Element(name = "series_title", required = false)
    @param:Element(name = "series_title", required = false)
    val seriesTitle: String? = null,

    @field:Element(name = "series_synonyms", required = false)
    @param:Element(name = "series_synonyms", required = false)
    val seriesSynonyms: String? = null,

    @field:Element(name = "series_type", required = false)
    @param:Element(name = "series_type", required = false)
    val seriesType: Int? = null,

    @field:Element(name = "series_episodes", required = false)
    @param:Element(name = "series_episodes", required = false)
    val seriesEpisodes: Int? = null,

    @field:Element(name = "series_status", required = false)
    @param:Element(name = "series_status", required = false)
    val seriesStatus: Int? = null,

    @field:Element(name = "series_start", required = false)
    @param:Element(name = "series_start", required = false)
    val seriesStart: String? = null,

    @field:Element(name = "series_end", required = false)
    @param:Element(name = "series_end", required = false)
    val seriesEnd: String? = null,

    @field:Element(name = "series_image", required = false)
    @param:Element(name = "series_image", required = false)
    val seriesImage: String? = null,

    @field:Element(name = "my_id", required = false)
    @param:Element(name = "my_id", required = false)
    val myId: Int? = null,

    @field:Element(name = "my_watched_episodes", required = false)
    @param:Element(name = "my_watched_episodes", required = false)
    var myWatchedEpisodes: Int? = null,

    @field:Element(name = "my_start_date", required = false)
    @param:Element(name = "my_start_date", required = false)
    val myStartDate: String? = null,

    @field:Element(name = "my_finish_date", required = false)
    @param:Element(name = "my_finish_date", required = false)
    val myFinishDate: String? = null,

    @field:Element(name = "my_score", required = false)
    @param:Element(name = "my_score", required = false)
    val myScore: Int? = null,

    @field:Element(name = "my_status", required = false)
    @param:Element(name = "my_status", required = false)
    var myStatus: Int? = null,

    @field:Element(name = "my_rewatching", required = false)
    @param:Element(name = "my_rewatching", required = false)
    val myRewatching: Int? = null,

    @field:Element(name = "my_rewatching_ep", required = false)
    @param:Element(name = "my_rewatching_ep", required = false)
    val myRewatchingEp: Int? = null,

    @field:Element(name = "my_last_updated", required = false)
    @param:Element(name = "my_last_updated", required = false)
    val myLastUpdated: Int? = null,

    @field:Element(name = "my_tags", required = false)
    @param:Element(name = "my_tags", required = false)
    val myTags: String? = null
) : Parcelable {
    private val baseUrl: String = "https://myanimelist.net/anime/"

    val malUrl: String = baseUrl + seriesAnimeDbId
    val totalEpisodes: String = if (seriesEpisodes == 0) {
        "??"
    } else {
        seriesEpisodes.toString()
    }

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(seriesAnimeDbId)
        parcel.writeString(seriesTitle)
        parcel.writeString(seriesSynonyms)
        parcel.writeValue(seriesType)
        parcel.writeValue(seriesEpisodes)
        parcel.writeValue(seriesStatus)
        parcel.writeString(seriesStart)
        parcel.writeString(seriesEnd)
        parcel.writeString(seriesImage)
        parcel.writeValue(myId)
        parcel.writeValue(myWatchedEpisodes)
        parcel.writeString(myStartDate)
        parcel.writeString(myFinishDate)
        parcel.writeValue(myScore)
        parcel.writeValue(myStatus)
        parcel.writeValue(myRewatching)
        parcel.writeValue(myRewatchingEp)
        parcel.writeValue(myLastUpdated)
        parcel.writeString(myTags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Anime> {
        override fun createFromParcel(parcel: Parcel): Anime {
            return Anime(parcel)
        }

        override fun newArray(size: Int): Array<Anime?> {
            return arrayOfNulls(size)
        }
    }
}