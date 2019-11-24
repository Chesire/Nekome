package com.chesire.nekome.app.series.detail

/**
 * List of errors for use with the SeriesDetail flow.
 */
enum class SeriesDetailError {
    None,
    Error,
    NewProgressBelowZero,
    NewProgressNaN,
    NewProgressTooHigh
}
