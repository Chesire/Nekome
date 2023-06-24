package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.kitsu.library.dto.AddResponseDto
import com.chesire.nekome.kitsu.library.dto.DataDto
import com.chesire.nekome.kitsu.library.dto.IncludedDto
import javax.inject.Inject

/**
 * Provides ability to map instances of [AddResponseDto] into [SeriesDomain].
 */
class ResponseDtoMapper @Inject constructor() {

    /**
     * Converts an instance of [AddResponseDto] into a [SeriesDomain].
     */
    fun toSeriesDomain(input: AddResponseDto): SeriesDomain? {
        val id = input.data.relationships.anime?.data?.id
            ?: input.data.relationships.manga?.data?.id
            ?: return null

        return input.included
            .find { it.id == id }
            ?.let { included ->
                createSeriesDomain(included, input.data)
            }
    }

    private fun createSeriesDomain(included: IncludedDto, data: DataDto) =
        SeriesDomain(
            included.id,
            data.id,
            included.type,
            included.attributes.subtype,
            included.attributes.slug,
            included.attributes.canonicalTitle,
            included.attributes.titles,
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
