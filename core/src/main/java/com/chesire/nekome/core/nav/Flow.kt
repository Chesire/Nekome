package com.chesire.nekome.core.nav

/**
 * Methods relating to changing flow within the application.
 */
interface Flow {
    /**
     * Indicate that the login flow has been completed.
     */
    fun finishLogin()
}
