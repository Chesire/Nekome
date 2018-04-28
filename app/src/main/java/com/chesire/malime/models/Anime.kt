package com.chesire.malime.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
@Root(name = "anime")
data class Anime(
    @PrimaryKey
    @field:Element(name = "series_animedb_id", required = false)
    @param:Element(name = "series_animedb_id", required = false)
    var seriesAnimeDbId: Int? = null,

    @field:Element(name = "series_title", required = false)
    @param:Element(name = "series_title", required = false)
    var seriesTitle: String? = null,

    @field:Element(name = "series_synonyms", required = false)
    @param:Element(name = "series_synonyms", required = false)
    var seriesSynonyms: String? = null,

    @field:Element(name = "series_type", required = false)
    @param:Element(name = "series_type", required = false)
    var seriesType: Int? = null,

    @field:Element(name = "series_episodes", required = false)
    @param:Element(name = "series_episodes", required = false)
    var seriesEpisodes: Int? = null,

    @field:Element(name = "series_status", required = false)
    @param:Element(name = "series_status", required = false)
    var seriesStatus: Int? = null,

    @Deprecated("Use seriesStartDate instead")
    @field:Element(name = "series_start", required = false)
    @param:Element(name = "series_start", required = false)
    var seriesStart: String? = null,

    @Deprecated("Use seriesEndDate instead")
    @field:Element(name = "series_end", required = false)
    @param:Element(name = "series_end", required = false)
    var seriesEnd: String? = null,

    @field:Element(name = "series_image", required = false)
    @param:Element(name = "series_image", required = false)
    var seriesImage: String? = null,

    @field:Element(name = "my_id", required = false)
    @param:Element(name = "my_id", required = false)
    var myId: Int? = null,

    @field:Element(name = "my_watched_episodes", required = false)
    @param:Element(name = "my_watched_episodes", required = false)
    var myWatchedEpisodes: Int? = null,

    @field:Element(name = "my_start_date", required = false)
    @param:Element(name = "my_start_date", required = false)
    var myStartDate: String? = null,

    @field:Element(name = "my_finish_date", required = false)
    @param:Element(name = "my_finish_date", required = false)
    var myFinishDate: String? = null,

    @field:Element(name = "my_score", required = false)
    @param:Element(name = "my_score", required = false)
    var myScore: Int? = null,

    @field:Element(name = "my_status", required = false)
    @param:Element(name = "my_status", required = false)
    var myStatus: Int? = null,

    @field:Element(name = "my_rewatching", required = false)
    @param:Element(name = "my_rewatching", required = false)
    var myRewatching: Int? = null,

    @field:Element(name = "my_rewatching_ep", required = false)
    @param:Element(name = "my_rewatching_ep", required = false)
    var myRewatchingEp: Int? = null,

    @field:Element(name = "my_last_updated", required = false)
    @param:Element(name = "my_last_updated", required = false)
    var myLastUpdated: Int? = null,

    @field:Element(name = "my_tags", required = false)
    @param:Element(name = "my_tags", required = false)
    var myTags: String? = null
) : Parcelable {
    @Ignore
    private val baseUrl: String = "https://myanimelist.net/anime/"

    fun getMalUrl(): String = baseUrl + seriesAnimeDbId

    fun getTotalEpisodes(): String =
        if (seriesEpisodes == 0) {
            "??"
        } else {
            seriesEpisodes.toString()
        }

    fun getSeriesStartDate(): Date = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(seriesStart)

    fun getSeriesEndDate(): Date {
        val dateToUse: String = if (seriesEnd == "0000-00-00") {
            "9999-99-99"
        } else {
            seriesEnd!!
        }

        return SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(dateToUse)
    }

    /**
     * Creates a new [Anime] object to temporarily store in room.
     */
    constructor(searchItem: Entry) : this(
        seriesAnimeDbId = searchItem.id,
        seriesTitle = searchItem.title,
        seriesSynonyms = searchItem.synonyms,
        seriesEpisodes = searchItem.episodes,
        seriesImage = searchItem.image
    )

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
    )

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