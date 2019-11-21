package com.chesire.malime.app.search

/**
 * Possible error states for Search to return.
 */
enum class SearchError {
    EmptyTitle,
    GenericError,
    NoTypeSelected,
    NoSeriesFound
}
