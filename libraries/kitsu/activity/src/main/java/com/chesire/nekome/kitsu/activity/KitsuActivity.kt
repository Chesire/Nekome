package com.chesire.nekome.kitsu.activity

import javax.inject.Inject

class KitsuActivity @Inject constructor(
    private val activityService: KitsuActivityService,
) {

    suspend fun retrieveActivity() {
        val result = activityService.retrieveActivity()
        val ey = result
    }
}
