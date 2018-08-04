package com.chesire.malime.core

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceProvider @Inject constructor() {
    fun getPreferencesFor(context: Context, authFile: String, contextMode: Int): SharedPreferences {
        return context.getSharedPreferences(authFile, contextMode)
    }
}