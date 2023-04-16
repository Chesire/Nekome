package com.chesire.nekome.datasource.auth.remote

data class AuthDomain(
    val accessToken: String,
    val refreshToken: String
)

sealed class AuthFailure {
    object CouldNotReachServer : AuthFailure()
    object InvalidCredentials : AuthFailure()
    object CouldNotRefresh : AuthFailure()
    object BadRequest : AuthFailure()
}
