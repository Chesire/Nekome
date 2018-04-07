package com.chesire.malime

import com.chesire.malime.models.UpdateAnime

/*
    Id is the value that is stored up on MAL,
    but due to displaying arrays and just using index it is easier to use the surface id
 */

/**
 * Enum to store the possible states an anime series can be in,
 * id will be the value that gets uploaded to [UpdateAnime.status] to update its status on MAL.
 */
enum class AnimeStates(
    /**
     * The state based on MAL.
     */
    val id: Int,
    /**
     * The state based on an internal value.
     */
    val surfaceId: Int
) {
    WATCHING(1, 0),
    COMPLETED(2, 1),
    ON_HOLD(3, 2),
    DROPPED(4, 3),
    PLAN_TO_WATCH(6, 4);

    companion object {
        fun getAnimeStateForId(id: Int): AnimeStates? {
            return AnimeStates.values().find { it.id == id }
        }

        fun getAnimeStateForSurfaceId(surfaceId: Int): AnimeStates? {
            return AnimeStates.values().find { it.surfaceId == surfaceId }
        }
    }
}