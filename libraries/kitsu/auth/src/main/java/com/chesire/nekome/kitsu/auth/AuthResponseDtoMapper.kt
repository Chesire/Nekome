package com.chesire.nekome.kitsu.auth

import com.chesire.nekome.auth.api.AuthDomain
import com.chesire.nekome.kitsu.auth.dto.AuthResponseDto
import javax.inject.Inject

// TODO #387
/**
 * Provides ability to map instances of [AuthResponseDto] into [AuthDomain].
 */
class AuthResponseDtoMapper @Inject constructor() {

    /**
     * Converts an instance of [AuthResponseDto] to [AuthDomain].
     */
    fun toAuthDomain(input: AuthResponseDto) = AuthDomain()
}
