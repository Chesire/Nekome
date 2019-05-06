package com.chesire.malime

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutHandler @Inject constructor() : AuthCaster.AuthCasterListener {
    override fun unableToRefresh() {
        Timber.w("unableToRefresh hit")
        // perform log out method
    }
}
