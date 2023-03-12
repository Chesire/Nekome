package com.chesire.nekome.core

import javax.inject.Qualifier

/**
 * Specifies that a coroutine should be using the IO Context.
 */
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class IOContext
