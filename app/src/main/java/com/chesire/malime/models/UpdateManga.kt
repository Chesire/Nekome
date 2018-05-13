package com.chesire.malime.models

import com.chesire.malime.MalStates
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class UpdateManga(
    val id: Int,
    val title: String,
    var chapter: Int,
    var totalChapters: Int,
    var volume: Int,
    var totalVolumes: Int,
    var dateFinish: String,
    var status: Int,
    val score: Int
) {
    constructor(mangaModel: Manga) : this(
        // This should never result in being 0
        id = mangaModel.seriesMangaDbId ?: 0,
        title = mangaModel.seriesTitle ?: "Unknown",
        chapter = mangaModel.myReadChapters ?: 0,
        totalChapters = mangaModel.seriesChapters ?: 0,
        volume = mangaModel.myReadVolumes ?: 0,
        totalVolumes = mangaModel.seriesVolumes ?: 0,
        dateFinish = "",
        status = mangaModel.myStatus ?: 0,
        score = mangaModel.myScore ?: 0
    )

    constructor(entry: Entry) : this(
        id = entry.id!!,
        title = entry.title ?: "Unknown",
        chapter = 0,
        totalChapters = entry.chapters ?: 0,
        volume = 0,
        totalVolumes = entry.chapters ?: 0,
        dateFinish = "",
        status = 0,
        score = 0
    )

    /**
     * Sets the status of the series.
     *
     * @param state this should match a value in [MalStates.surfaceId]
     */
    fun setSeriesStatus(state: Int) {
        val newState = MalStates.getMalStateForSurfaceId(state)
        if (newState == null) {
            Timber.e("The MalState is null, looked for state [%d]", state)
        } else {
            status = newState.id
        }
    }

    fun setToCompleteState() {
        status = MalStates.COMPLETED.id
        val calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        dateFinish = dateFormatter.format(calendar.time)
    }

    fun setToReadingState() {
        status = MalStates.READING.id
        dateFinish = ""
    }

    fun getXml(): String {
        return "<entry>" +
                "<chapter>$chapter</chapter>" +
                "<volume>$volume</volume>" +
                "<status>$status</status>" +
                "<score>$score</score>" +
                "<times_reread></times_reread>" +
                "<reread_value></reread_value>" +
                "<date_start></date_start>" +
                "<date_finish>$dateFinish</date_finish>" +
                "<priority></priority>" +
                "<enable_discussion></enable_discussion>" +
                "<enable_rewatching></enable_rewatching>" +
                "<comments></comments>" +
                "<scan_group></scan_group>" +
                "<tags></tags>" +
                "</entry>"
    }
}