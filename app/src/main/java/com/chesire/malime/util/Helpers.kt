package com.chesire.malime.util

val isRunningTest by lazy {
    try {
        Class.forName("android.support.test.espresso.Espresso")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}