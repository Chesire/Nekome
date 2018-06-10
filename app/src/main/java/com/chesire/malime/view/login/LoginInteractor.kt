package com.chesire.malime.view.login

import com.chesire.malime.core.flags.SupportedService

interface LoginInteractor {
    fun serviceSelected(service: SupportedService)
    fun loginSuccessful()
}