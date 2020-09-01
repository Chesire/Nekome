package com.chesire.nekome.app.search

/**
 * Possible error states for Search to return.
 */
enum class SearchError {
    EmptyTitle,
    GenericError,
    NoTypeSelected,
    NoSeriesFound
}
