package com.chesire.malime.view.search

import com.chesire.malime.models.Entry

interface SearchInteractionListener {
    fun onAddPressed(selectedEntry: Entry, callback: (Boolean) -> Unit)
    fun onImageClicked(entry: Entry)
}