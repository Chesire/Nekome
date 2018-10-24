package com.chesire.malime.util

import javax.inject.Qualifier

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ComputationScheduler

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class IOScheduler

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class UIScheduler
