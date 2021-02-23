package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.library.dto.AddResponseDto
import com.chesire.nekome.kitsu.library.dto.DataDto
import com.chesire.nekome.kitsu.library.dto.IncludedDto
import com.chesire.nekome.library.api.LibraryDomain
import javax.inject.Inject

/**
 * Provides ability to map instances of [AddResponseDto] into [LibraryDomain].
 */
class ResponseDtoMapper @Inject constructor() {

    /**
     * Converts an instance of [AddResponseDto] into a [LibraryDomain].
     */
    fun toLibraryDomain(input: AddResponseDto): LibraryDomain? {
        val id = input.data.relationships.anime?.data?.id
            ?: input.data.relationships.manga?.data?.id
            ?: return null

        return input.included
            .find { it.id == id }
            ?.let { included ->
                createLibraryDomain(included, input.data)
            }
    }

    private fun createLibraryDomain(included: IncludedDto, data: DataDto) =
        LibraryDomain(
            included.id,
            data.id,
            included.type,
            included.attributes.subtype,
            included.attributes.slug,
            included.attributes.canonicalTitle,
            included.attributes.status,
            data.attributes.status,
            data.attributes.progress,
            included.attributes.episodeCount ?: included.attributes.chapterCount ?: 0,
            data.attributes.rating ?: 0,
            included.attributes.posterImage ?: ImageModel.empty,
            included.attributes.startDate ?: "",
            included.attributes.endDate ?: ""
        )
}
