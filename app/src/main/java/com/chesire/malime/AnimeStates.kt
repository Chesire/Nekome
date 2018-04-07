package com.chesire.malime

enum class AnimeStates(
    val id: Int,
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