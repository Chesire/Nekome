package com.chesire.malime.util.sec

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.nio.charset.Charset
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

// this cipher should work, will need to test < API21
private const val transformation = "AES/GCM/NoPadding"
private const val androidKeyStore = "AndroidKeyStore"

class Encryptor {
    /**
     * Encrypts the [text].
     *
     * @return a pair with [Pair.first] as the encrypted text byte array, and [Pair.second] as the iv
     */
    fun encryptText(alias: String, text: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(transformation).apply {
            init(Cipher.ENCRYPT_MODE, getSecretKey(alias))
        }

        return Pair(
            cipher.doFinal(
                text.toByteArray(Charset.forName("UTF-8"))
            ),
            cipher.iv
        )
    }

    private fun getSecretKey(alias: String): SecretKey {
        val keyGenerator: KeyGenerator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator =
                    KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, androidKeyStore)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
        } else {
            keyGenerator = KeyGenerator.getInstance(androidKeyStore).apply {
                init(SecureRandom())
            }
        }

        return keyGenerator.generateKey()
    }
}