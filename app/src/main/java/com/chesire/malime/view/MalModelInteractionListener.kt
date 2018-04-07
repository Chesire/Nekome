package com.chesire.malime.view

interface MalModelInteractionListener<in T, in U> {
    fun onImageClicked(model: T)
    fun onLongClick(originalModel: T, updateModel: U, callback: () -> Unit)
    fun onSeriesUpdate(originalModel: T, updateModel: U, callback: () -> Unit)
}