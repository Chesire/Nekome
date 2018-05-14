package com.chesire.malime.util.sec

import java.nio.charset.Charset
import java.security.KeyStore
import java.security.PrivateKey
import javax.crypto.Cipher

private const val transformation = "RSA/ECB/PKCS1Padding"
private const val androidKeyStore = "AndroidKeyStore"

class Decryptor {
    private val keyStore = KeyStore.getInstance(androidKeyStore)

    init {
        keyStore.load(null)
    }

    fun decryptData(alias: String, encryptedData: ByteArray): String {
        val cipher = Cipher.getInstance(transformation).apply {
            init(Cipher.DECRYPT_MODE, getSecretKey(alias))
        }

        return String(cipher.doFinal(encryptedData), Charset.forName("UTF-8"))
    }

    private fun getSecretKey(alias: String): PrivateKey? {
        return keyStore.getKey(alias, null) as PrivateKey?
    }
}