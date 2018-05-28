package com.chesire.malime.mal

import com.chesire.malime.mal.models.UpdateAnime
import com.chesire.malime.mal.models.UpdateManga

/*
    Id is the value that is stored up on MAL,
    but due to displaying arrays and just using index it is easier to use the surface id
 */

/**
 * Enum to store the possible states an anime or manga series can be in,
 * id will be the value that gets uploaded to [UpdateAnime.status]|[UpdateManga.status] to update its status on MAL.
 */
enum class MalStates(
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
    READING(1, 0),
    COMPLETED(2, 1),
    ON_HOLD(3, 2),
    DROPPED(4, 3),
    PLAN_TO_WATCH(6, 4),
    PLAN_TO_READ(6, 4);

    companion object {
        fun getMalStateForId(id: Int): MalStates? {
            return MalStates.values().find { it.id == id }
        }

        fun getMalStateForSurfaceId(surfaceId: Int): MalStates? {
            return MalStates.values().find { it.surfaceId == surfaceId }
        }
    }
}