package com.chesire.nekome.kitsu

import android.content.Context
import com.thz.keystorehelper.KeyStoreManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val ALIAS = "kitsuPrivateAuth"

/**
 * Used to encrypt and decrypt strings using the [KeyStoreManager].
 */
class Cryption @Inject constructor(@ApplicationContext context: Context) {
    init {
        KeyStoreManager.init(context)
    }

    /**
     * Encrypts [text] using the [KeyStoreManager].
     */
    fun encrypt(text: String): String = KeyStoreManager.encryptData(text, ALIAS)

    /**
     * Decrypts [text] using the [KeyStoreManager].
     */
    fun decrypt(text: String): String = KeyStoreManager.decryptData(text, ALIAS)
}
