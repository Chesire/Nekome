package com.chesire.malime.mal.models

import android.arch.persistence.room.Ignore
import android.os.Parcel
import android.os.Parcelable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "entry")
data class Entry(
    @field:Element(name = "id", required = false)
    @param:Element(name = "id", required = false)
    val id: Int? = null,
    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    val title: String? = null,
    @field:Element(name = "english", required = false)
    @param:Element(name = "english", required = false)
    val english: String? = null,
    @field:Element(name = "synonyms", required = false)
    @param:Element(name = "synonyms", required = false)
    val synonyms: String? = null,
    @field:Element(name = "episodes", required = false)
    @param:Element(name = "episodes", required = false)
    val episodes: Int? = null,
    @field:Element(name = "chapters", required = false)
    @param:Element(name = "chapters", required = false)
    val chapters: Int? = null,
    @field:Element(name = "volumes", required = false)
    @param:Element(name = "volumes", required = false)
    val volumes: Int? = null,
    @field:Element(name = "score", required = false)
    @param:Element(name = "score", required = false)
    val score: Double? = null,
    @field:Element(name = "type", required = false)
    @param:Element(name = "type", required = false)
    val type: String? = null,
    @field:Element(name = "status", required = false)
    @param:Element(name = "status", required = false)
    val status: String? = null,
    @field:Element(name = "start_date", required = false)
    @param:Element(name = "start_date", required = false)
    val start_date: String? = null,
    @field:Element(name = "end_date", required = false)
    @param:Element(name = "end_date", required = false)
    val end_date: String? = null,
    @field:Element(name = "synopsis", required = false)
    @param:Element(name = "synopsis", required = false)
    val synopsis: String? = null,
    @field:Element(name = "image", required = false)
    @param:Element(name = "image", required = false)
    val image: String? = null
) : Parcelable {

    @Ignore
    private val baseUrl: String = "https://myanimelist.net"

    fun getMalUrl(type: String): String = "$baseUrl/$type/$id"

    constructor(source: Parcel) : this(
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(title)
        writeString(english)
        writeString(synonyms)
        writeValue(episodes)
        writeValue(chapters)
        writeValue(volumes)
        writeValue(score)
        writeString(type)
        writeString(status)
        writeString(start_date)
        writeString(end_date)
        writeString(synopsis)
        writeString(image)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Entry> = object : Parcelable.Creator<Entry> {
            override fun createFromParcel(source: Parcel): Entry =
                Entry(source)
            override fun newArray(size: Int): Array<Entry?> = arrayOfNulls(size)
        }
    }
}