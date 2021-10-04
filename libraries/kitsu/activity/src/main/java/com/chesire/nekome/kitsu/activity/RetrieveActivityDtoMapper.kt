package com.chesire.nekome.kitsu.activity

import com.chesire.nekome.datasource.activity.ActivityDomain
import com.chesire.nekome.datasource.activity.Event
import com.chesire.nekome.kitsu.activity.dto.ChangedDataContainer
import com.chesire.nekome.kitsu.activity.dto.RetrieveActivityDto
import javax.inject.Inject

/**
 * Performs mapping of activity related dtos to domain objects.
 */
class RetrieveActivityDtoMapper @Inject constructor() {

    /**
     * Maps instances of [RetrieveActivityDto.Data] into an [ActivityDomain].
     */
    fun toActivityDomain(input: RetrieveActivityDto.Data) =
        ActivityDomain(
            input.id,
            input.attributes.updatedAt,
            createEvents(input.attributes.changedData)
        )

    private fun createEvents(input: ChangedDataContainer): List<Event> {
        return input.changedData.map { dataItem ->
            Event(dataItem.from, dataItem.to, dataItem.type)
        }
    }
}
