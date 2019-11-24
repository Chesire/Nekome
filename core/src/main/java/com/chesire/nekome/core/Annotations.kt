package com.chesire.nekome.core

import javax.inject.Qualifier

/**
 * Specifies that a coroutine should be using the IO Context.
 */
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class IOContext

/**
 * Specifies that a coroutine should be using the Main Context.
 */
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class MainContext
