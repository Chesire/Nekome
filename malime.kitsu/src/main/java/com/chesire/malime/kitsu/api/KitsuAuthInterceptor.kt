package com.chesire.malime.kitsu.api

import android.os.AsyncTask
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.kitsu.KITSU_ENDPOINT
import com.chesire.malime.kitsu.models.request.RefreshAuthRequest
import com.chesire.malime.kitsu.models.response.LoginResponse
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import timber.log.Timber
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Provides an interceptor that handles the auth and refreshing the token when needed.
 */
class KitsuAuthInterceptor(private val authorizer: KitsuAuthorizer) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val authModel = authorizer.retrieveAuthDetails()
        if (authModel.expireAt != 0L && System.currentTimeMillis() / 1000 > authModel.expireAt) {
            Timber.w("Need to refresh auth token")
            updateAuthToken(authModel)

            // force return an error as we are updating the auth token
            return Response.Builder()
                .code(403)
                .body(null)
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .message("Refreshing token")
                .build()
        }

        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer ${authModel.authToken}")
            .build()

        return chain.proceed(authenticatedRequest)
    }

    @Suppress("UnsafeCast")
    private fun updateAuthToken(authModel: AuthModel) {
        // The auth token has expired, update it using the refresh token
        // hopefully the service in the app will handle this so this won't often be called
        AsyncTask.execute {
            val gson = Gson()
            val url = URL(KITSU_ENDPOINT + "api/oauth/token")
            val refreshRequest = RefreshAuthRequest(authModel.refreshToken)
            var conn: HttpsURLConnection? = null

            try {
                conn = (url.openConnection() as HttpsURLConnection).apply {
                    setRequestProperty("Content-Type", "application/vnd.api+json")
                    requestMethod = "POST"
                    doOutput = true
                    outputStream.write(gson.toJson(refreshRequest).toByteArray())
                }

                Timber.d("Received response for refreshing token - ${conn.responseCode}")

                if (conn.responseCode == 200) {
                    InputStreamReader(conn.inputStream, "UTF-8").use { responseReader ->
                        JsonReader(responseReader).use { reader ->
                            val loginResponse =
                                gson.fromJson<LoginResponse>(reader, LoginResponse::class.java)
                            val newAuthModel =
                                AuthModel(
                                    loginResponse.accessToken,
                                    loginResponse.refreshToken,
                                    loginResponse.createdAt + loginResponse.expiresIn,
                                    "kitsu"
                                )
                            authorizer.storeAuthDetails(newAuthModel)
                            Timber.d("Successfully refreshed auth token")
                        }
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex, "Error trying to refresh auth token")
            } finally {
                conn?.disconnect()
            }
        }
    }
}
