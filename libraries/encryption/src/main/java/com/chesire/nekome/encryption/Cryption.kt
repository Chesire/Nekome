package com.chesire.nekome.encryption

/**
 * Provides the means to [encrypt] or [decrypt] some text.
 */
interface Cryption {
    /**
     * Encrypts [text] using [alias] and the provided encryption method.
     */
    fun encrypt(text: String, alias: String): String

    /**
     * Decrypts [text] using [alias] and the provided encryption method.
     */
    fun decrypt(text: String, alias: String): String
}
