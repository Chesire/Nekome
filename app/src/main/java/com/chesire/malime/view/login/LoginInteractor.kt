package com.chesire.malime.view.login

import com.chesire.malime.util.SupportedService

interface LoginInteractor {
    fun serviceSelected(service: SupportedService)
    fun loginSuccessful()
    fun acquiredLibraries()
}