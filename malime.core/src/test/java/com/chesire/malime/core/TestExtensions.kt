package com.chesire.malime.core

import org.mockito.Mockito

inline fun <reified T : Any> customMock() = Mockito.mock(T::class.java)