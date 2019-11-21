package com.chesire.malime.kitsu

import android.content.Context
import com.thz.keystorehelper.KeyStoreManager
import javax.inject.Inject

private const val ALIAS = "kitsuPrivateAuth"

class Cryption @Inject constructor(context: Context) {
    init {
        KeyStoreManager.init(context)
    }

    fun encrypt(text: String): String = KeyStoreManager.encryptData(text, ALIAS)
    fun decrypt(text: String): String = KeyStoreManager.decryptData(text, ALIAS)
}
