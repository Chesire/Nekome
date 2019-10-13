package com.chesire.malime.app.series.detail

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
