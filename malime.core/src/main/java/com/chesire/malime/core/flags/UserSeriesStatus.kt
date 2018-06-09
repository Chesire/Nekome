package com.chesire.malime.core.flags

enum class UserSeriesStatus(
    val internalId: Int,
    val malId: Int,
    val kitsuString: String
) {
    Unknown(0, 0, "unknown"),
    Current(1, 1, "current"),
    Completed(2, 2, "completed"),
    OnHold(3, 3, "on_hold"),
    Dropped(4, 4, "dropped"),
    Planned(5, 6, "planned");

    companion object {
        fun getStatusForKitsuString(inputString: String): UserSeriesStatus {
            return UserSeriesStatus.values().find { it.kitsuString == inputString } ?: Unknown
        }

        fun getStatusForMalId(id: Int): UserSeriesStatus {
            return UserSeriesStatus.values().find { it.malId == id } ?: Unknown
        }

        fun getStatusForInternalId(id: Int): UserSeriesStatus {
            return UserSeriesStatus.values().find { it.internalId == id } ?: Unknown
        }
    }
}