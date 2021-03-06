package com.chesire.nekome.kitsu.activity.adapter

import com.chesire.nekome.kitsu.activity.dto.ChangedData
import com.chesire.nekome.kitsu.activity.dto.Event
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson

private const val RATING = "rating"
private const val PROGRESS = "progress"
private const val STATUS = "status"

class EventAdapter {

    @FromJson
    fun eventFromString(jsonReader: JsonReader): Event {
        val dataSets = mutableListOf<ChangedData>()
        jsonReader.beginObject()
        while (jsonReader.peek() == JsonReader.Token.NAME) {
            dataSets.add(createChangedDataItem(jsonReader))
        }
        jsonReader.endObject()

        return Event(dataSets)
    }

    @ToJson
    fun eventToString(event: Event): String {
        error("Unable to convert com.chesire.nekome.kitsu.activity.adapter.Event to String")
    }

    private fun createChangedDataItem(jsonReader: JsonReader): ChangedData {
        val name = jsonReader.nextName()
        jsonReader.beginArray()
        val from = jsonReader.readJsonValue()
        val to = jsonReader.readJsonValue()
        jsonReader.endArray()

        return when (name) {
            RATING -> ChangedData.Rating(from.cast(), to.cast())
            PROGRESS -> ChangedData.Progress(from.cast(), to.cast())
            STATUS -> ChangedData.Status(from.cast(), to.cast())
            else -> error("Unexpected event name found")
        }
    }

    private fun <T> Any?.cast(): T {
        return when (this) {
            null -> ""
            is String -> toString() // maybe could cast to series status?
            is Double -> toInt()
            is Int -> this
            else -> error("Invalid data type provided")
        } as T
    }
}
