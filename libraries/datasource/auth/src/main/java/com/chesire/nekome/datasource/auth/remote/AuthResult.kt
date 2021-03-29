package com.chesire.nekome.datasource.auth.remote

sealed class AuthResult {
    data class Success(val accessToken: String, val refreshToken: String) : AuthResult()
    object CouldNotReachServer : AuthResult()
    object InvalidCredentials : AuthResult()
    object CouldNotRefresh : AuthResult()
    object BadRequest : AuthResult()
}
