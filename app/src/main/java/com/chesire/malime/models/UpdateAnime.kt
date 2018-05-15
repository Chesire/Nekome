package com.chesire.malime.models

import com.chesire.malime.MalStates
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class UpdateAnime(
    val id: Int,
    val title: String,
    var episode: Int,
    val totalEpisodes: Int,
    var dateFinish: String,
    var status: Int,
    val score: Int
) {
    constructor(animeModel: Anime) : this(
        // This should never result in being 0
        id = animeModel.seriesAnimeDbId ?: 0,
        title = animeModel.seriesTitle ?: "Unknown",
        episode = animeModel.myWatchedEpisodes ?: 0,
        totalEpisodes = animeModel.seriesEpisodes ?: 0,
        dateFinish = "",
        status = animeModel.myStatus ?: 0,
        score = animeModel.myScore ?: 0
    )

    constructor(entry: Entry) : this(
        id = entry.id!!,
        title = entry.title ?: "Unknown",
        episode = 0,
        totalEpisodes = entry.episodes ?: 0,
        dateFinish = "",
        status = MalStates.WATCHING.id,
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

    fun setToWatchingState() {
        status = MalStates.WATCHING.id
        dateFinish = ""
    }

    fun getXml(): String {
        return "<entry>" +
                "<episode>$episode</episode>" +
                "<status>$status</status>" +
                "<score>$score</score>" +
                "<storage_type></storage_type>" +
                "<storage_value></storage_value>" +
                "<times_rewatched></times_rewatched>" +
                "<rewatch_value></rewatch_value>" +
                "<date_start></date_start>" +
                "<date_finish>$dateFinish</date_finish>" +
                "<priority></priority>" +
                "<enable_discussion></enable_discussion>" +
                "<enable_rewatching></enable_rewatching>" +
                "<comments></comments>" +
                "<tags></tags>" +
                "</entry>"
    }
}