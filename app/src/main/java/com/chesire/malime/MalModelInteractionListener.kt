package com.chesire.malime

interface MalModelInteractionListener<in T> {
    fun onImageClicked(model: T)
    fun onPlusOneClicked(model: T)
    fun onMinusOneClicked(model: T)
}