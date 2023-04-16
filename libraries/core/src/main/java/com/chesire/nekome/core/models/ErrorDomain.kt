package com.chesire.nekome.core.models

import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_NO_CONTENT
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.HttpURLConnection.HTTP_UNAVAILABLE

data class ErrorDomain(
    val message: String,
    val code: Int
) {
    companion object {
        val badRequest = ErrorDomain("", HTTP_BAD_REQUEST)
        val couldNotReach = ErrorDomain("Could not reach service", HTTP_UNAVAILABLE)
        val couldNotRefresh = ErrorDomain("Could not refresh auth", HTTP_FORBIDDEN)
        val emptyResponse = ErrorDomain("Response body is null", HTTP_NO_CONTENT)
        val invalidCredentials = ErrorDomain("", HTTP_UNAUTHORIZED)
    }
}
