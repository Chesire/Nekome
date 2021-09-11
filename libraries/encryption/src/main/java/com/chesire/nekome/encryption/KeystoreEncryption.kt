package com.chesire.nekome.encryption

import android.content.Context
import com.thz.keystorehelper.KeyStoreManager

/**
 * Used to encrypt and decrypt strings using the [KeyStoreManager].
 */
class KeystoreEncryption(private val context: Context) : Cryption {

    override fun encrypt(text: String, alias: String): String {
        KeyStoreManager.init(context)
        return KeyStoreManager.encryptData(text, alias)
    }

    override fun decrypt(text: String, alias: String): String {
        KeyStoreManager.init(context)
        return KeyStoreManager.decryptData(text, alias)
    }
}
