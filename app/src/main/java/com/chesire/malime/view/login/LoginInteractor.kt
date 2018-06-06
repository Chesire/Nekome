package com.chesire.malime.view.login

import com.chesire.malime.core.SupportedService

interface LoginInteractor {
    fun serviceSelected(service: SupportedService)
    fun loginSuccessful()
    fun acquiredLibraries()
}