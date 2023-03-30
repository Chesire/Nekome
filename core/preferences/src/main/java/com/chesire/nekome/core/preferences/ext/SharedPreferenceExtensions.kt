package com.chesire.nekome.core.preferences.ext

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Migrates keys to a new [SharedPreferences] instance.
 * Will only migrate keys that have a value of type [String].
 */
fun SharedPreferences.migrateTo(encryptedPreferences: SharedPreferences) {
    // If the current list is empty, no need to migrate
    if (all.isEmpty()) {
        return
    }

    val migrated = mutableListOf<String>()
    all.forEach { (key, value) ->
        if (value is String) {
            migrated.add(key)
            encryptedPreferences.edit {
                putString(key, value)
            }
        }
    }
    edit {
        migrated.forEach(::remove)
    }
}
