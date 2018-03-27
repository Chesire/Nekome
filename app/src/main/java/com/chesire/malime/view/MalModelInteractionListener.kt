package com.chesire.malime.view

interface MalModelInteractionListener<in T, in U> {
    fun onImageClicked(model: T)
    fun onSeriesUpdate(originalModel: T, updateModel: U, callback: () -> Unit)
}