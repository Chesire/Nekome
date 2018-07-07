package com.chesire.malime.injection

import com.chesire.malime.util.updateservice.PeriodicUpdateService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(service: PeriodicUpdateService)
}