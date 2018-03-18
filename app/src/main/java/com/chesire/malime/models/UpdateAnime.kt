package com.chesire.malime.models

data class UpdateAnime(
        val id: Int,
        val title: String,
        var episode: Int,
        val totalEpisodes: Int,
        var status: Int,
        val score: Int
) {
    constructor(animeModel: Anime) : this(
            // This should never result in being 0
            id = animeModel.seriesAnimeDbId ?: 0,
            title = animeModel.seriesTitle ?: "Unknown",
            episode = animeModel.myWatchedEpisodes ?: 0,
            totalEpisodes = animeModel.seriesEpisodes ?: 0,
            status = animeModel.myStatus ?: 0,
            score = animeModel.myScore ?: 0
    )

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
                "<date_finish></date_finish>" +
                "<priority></priority>" +
                "<enable_discussion></enable_discussion>" +
                "<enable_rewatching></enable_rewatching>" +
                "<comments></comments>" +
                "<tags></tags>" +
                "</entry>"
    }
}