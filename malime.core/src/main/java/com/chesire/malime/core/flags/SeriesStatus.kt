package com.chesire.malime.core.flags

enum class SeriesStatus(
    val internalId: Int,
    val malId: Int,
    val kitsuString: String
) {
    Unknown(0, 0, "unknown"),
    Current(1, 1, "current"),
    Finished(2, 2, "finished"),
    TBA(3, 3, "tba"),
    Unreleased(4, 4, "unreleased"),
    Upcoming(5, 6, "upcoming");

    companion object {
        fun getStatusForKitsuString(inputString: String): SeriesStatus {
            return SeriesStatus.values().find { it.kitsuString == inputString } ?: Unknown
        }

        fun getStatusForMalId(id: Int): SeriesStatus {
            return SeriesStatus.values().find { it.malId == id } ?: Unknown
        }

        fun getStatusForInternalId(id: Int): SeriesStatus {
            return SeriesStatus.values().find { it.internalId == id } ?: Unknown
        }
    }
}