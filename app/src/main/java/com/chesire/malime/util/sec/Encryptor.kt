package com.chesire.malime.util.sec

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.nio.charset.Charset
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

private const val transformation = "AES/GCM/NoPadding"
private const val androidKeyStore = "AndroidKeyStore"

class Encryptor {
    var encryption: ByteArray? = null
    var iv: ByteArray? = null

    fun encryptText(alias: String, text: String): ByteArray? {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

        iv = cipher.iv
        encryption = cipher.doFinal(
            text.toByteArray(Charset.forName("UTF-8"))
        )

        return encryption
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
            keyGenerator = KeyGenerator.getInstance(androidKeyStore)
            keyGenerator.init(SecureRandom())
        }

        return keyGenerator.generateKey()
    }
}