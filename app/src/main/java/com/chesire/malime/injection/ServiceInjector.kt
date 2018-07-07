package com.chesire.malime.injection

import com.chesire.malime.util.updateservice.PeriodicUpdateService

object ServiceInjector {
    fun init(service: PeriodicUpdateService) {
        DaggerServiceComponent.builder()
            .serviceModule(ServiceModule(service))
            .build()
            .inject(service)
    }
}