package com.chesire.malime.injection

import com.chesire.malime.MalimeApplication

object AppInjector {
    fun init(malApp: MalimeApplication) {
        DaggerAppComponent.builder()
            .application(malApp)
            .build()
            .inject(malApp)
    }