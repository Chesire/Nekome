package com.chesire.nekome.kitsu.activity.adapter

import com.chesire.nekome.kitsu.activity.dto.ChangedData
import com.chesire.nekome.kitsu.activity.dto.ChangedDataContainer
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson

/**
 * Adapter to read from a [JsonReader] to create instances of a [ChangedDataContainer].
 */
class ChangedDataContainerAdapter {

    /**
     * Reads the data from [jsonReader] and creates a new [ChangedDataContainer].
     */
    @FromJson
    fun changedDataContainerFromString(jsonReader: JsonReader): ChangedDataContainer {
        val dataSets = mutableListOf<ChangedData>()
        jsonReader.beginObject()
        while (jsonReader.peek() == JsonReader.Token.NAME) {
            dataSets.add(createChangedDataItem(jsonReader))
        }
        jsonReader.endObject()

        return ChangedDataContainer(dataSets)
    }

    /**
     * Not implemented.
     */
    @Suppress("UnusedPrivateMember")
    @ToJson
    fun changedDataContainerToString(event: ChangedDataContainer): String {
        error("Unable to convert ChangedDataContainer to String")
    }

    private fun createChangedDataItem(jsonReader: JsonReader): ChangedData {
        val name = jsonReader.nextName()
        jsonReader.beginArray()
        val from = jsonReader.readJsonValue()
        val to = jsonReader.readJsonValue()
        jsonReader.endArray()

        return ChangedData(name, from.stringify(), to.stringify())
    }

    @Suppress("UNCHECKED_CAST")
    private fun Any?.stringify(): String {
        return when (this) {
            null -> ""
            is Double -> toInt().toString()
            else -> toString()
        }
    }
}
