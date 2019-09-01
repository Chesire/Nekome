package com.chesire.malime.server.flags

enum class UserSeriesStatus(val index: Int) {
    Unknown(-1),
    Current(0),
    Completed(1),
    OnHold(2),
    Dropped(3),
    Planned(4)
}
